package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoucherService {

    @Autowired private VoucherRepository voucherRepository;
    @Autowired private VoucherDetailRepository voucherDetailRepository;
    @Autowired private CurrentAccountRepository currentAccountRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CurrentAccountService currentAccountService;
    @Autowired private ReceiptService receiptService;
    @Autowired private ReceiptRepository receiptRepository;

    @Transactional
    public VoucherResponseDTO createVoucher(VoucherCreateDTO dto) {

        if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
            throw new BadRequestException("El comprobante debe tener al menos un detalle.");
        }
        if (dto.getCurrencyType() == null) {
            throw new BadRequestException("El comprobante debe especificar la moneda (PESOS o DOLARES).");
        }

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        Voucher voucher = new Voucher();
        voucher.setPatient(patient);
        voucher.setVoucherDate(dto.getVoucherDate() != null ? dto.getVoucherDate() : LocalDate.now());
        voucher.setObservations(dto.getObservations());
        voucher.setCurrencyType(dto.getCurrencyType());

        if (dto.getUserId() != null) {
            User professional = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));
            voucher.setUser(professional);
        }

        Voucher savedVoucher = voucherRepository.save(voucher);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (VoucherDetailDTO detailDto : dto.getDetails()) {
            VoucherDetail detail = new VoucherDetail();
            detail.setVoucher(savedVoucher);
            detail.setDetail(detailDto.getDetail());
            detail.setUnitPrice(detailDto.getUnitPrice());
            detail.setAmount(detailDto.getAmount());
            detail.setDueDate(detailDto.getDueDate());
            voucherDetailRepository.save(detail);

            BigDecimal subtotal = detailDto.getUnitPrice().multiply(BigDecimal.valueOf(detailDto.getAmount()));
            totalAmount = totalAmount.add(subtotal);
        }

        savedVoucher.setTotal_amount(totalAmount);
        voucherRepository.save(savedVoucher);

        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            CurrentAccount accountEntry = new CurrentAccount();
            accountEntry.setPatient(patient);
            accountEntry.setVoucher(savedVoucher);
            accountEntry.setType(CurrentAccountType.VOUCHER);
            accountEntry.setCanceled(false);

            CurrentAccount lastRecord = currentAccountRepository.findTopByPatientIdOrderByIdDesc(patient.getId()).orElse(null);

            BigDecimal prevBalancePesos = BigDecimal.ZERO;
            BigDecimal prevBalanceDollars = BigDecimal.ZERO;

            if (lastRecord != null && (lastRecord.getCanceled() == null || !lastRecord.getCanceled())) {
                prevBalancePesos = lastRecord.getBalancePesos() != null ? lastRecord.getBalancePesos() : BigDecimal.ZERO;
                prevBalanceDollars = lastRecord.getBalanceDollars() != null ? lastRecord.getBalanceDollars() : BigDecimal.ZERO;
            }

            if (dto.getCurrencyType() == CurrencyType.PESOS) {
                accountEntry.setTransactionAmountPesos(totalAmount);
                accountEntry.setTransactionAmountDollars(BigDecimal.ZERO);
                accountEntry.setBalancePesos(prevBalancePesos.add(totalAmount));
                accountEntry.setBalanceDollars(prevBalanceDollars);
            } else {
                accountEntry.setTransactionAmountPesos(BigDecimal.ZERO);
                accountEntry.setTransactionAmountDollars(totalAmount);
                accountEntry.setBalancePesos(prevBalancePesos);
                accountEntry.setBalanceDollars(prevBalanceDollars.add(totalAmount));
            }

            currentAccountRepository.save(accountEntry);
        }

        currentAccountService.updateDebtorLabel(patient);

        return new VoucherResponseDTO(savedVoucher.getId(), savedVoucher.getVoucherDate(), totalAmount, savedVoucher.getCurrencyType());
    }

    @Transactional
    public VoucherResponseDTO updateVoucher(Long id, VoucherCreateDTO dto) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el comprobante con ID: " + id));

        voucher.setVoucherDate(dto.getVoucherDate() != null ? dto.getVoucherDate() : voucher.getVoucherDate());
        voucher.setObservations(dto.getObservations());
        voucher.setCurrencyType(dto.getCurrencyType());

        if (dto.getUserId() != null) {
            User professional = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));
            voucher.setUser(professional);
        }

        List<VoucherDetail> existingDetails = voucherDetailRepository.findByVoucherId(voucher.getId());
        for (VoucherDetail existingDetail : existingDetails) {
            boolean keep = dto.getDetails().stream()
                    .anyMatch(d -> d.getId() != null && d.getId().equals(existingDetail.getId()));
            if (!keep) {
                voucherDetailRepository.delete(existingDetail);
            }
        }

        BigDecimal newTotalAmount = BigDecimal.ZERO;

        for (VoucherDetailDTO detailDto : dto.getDetails()) {
            VoucherDetail detail;
            if (detailDto.getId() != null) {
                detail = voucherDetailRepository.findById(detailDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado"));
            } else {
                detail = new VoucherDetail();
                detail.setVoucher(voucher);
            }
            detail.setDetail(detailDto.getDetail());
            detail.setUnitPrice(detailDto.getUnitPrice());
            detail.setAmount(detailDto.getAmount());
            detail.setDueDate(detailDto.getDueDate());
            voucherDetailRepository.save(detail);

            BigDecimal subtotal = detailDto.getUnitPrice().multiply(BigDecimal.valueOf(detailDto.getAmount()));
            newTotalAmount = newTotalAmount.add(subtotal);
        }

        voucher.setTotal_amount(newTotalAmount);
        Voucher updatedVoucher = voucherRepository.save(voucher);

        currentAccountService.rebuildPatientBalances(updatedVoucher.getPatient().getId());

        return new VoucherResponseDTO(updatedVoucher.getId(), updatedVoucher.getVoucherDate(), newTotalAmount, updatedVoucher.getCurrencyType());
    }

    public VoucherDTO getVoucherById(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el comprobante con ID: " + id));

        List<VoucherDetail> details = voucherDetailRepository.findByVoucherId(id);

        VoucherDTO responseDTO = new VoucherDTO();
        responseDTO.setId(voucher.getId());
        responseDTO.setPatientFullName(voucher.getPatient().getFullName());

        if (voucher.getUser() != null) {
            responseDTO.setProfessionalFullName(voucher.getUser().getName() + " " + voucher.getUser().getLastname());
        } else {
            responseDTO.setProfessionalFullName("No especificado");
        }

        responseDTO.setVoucherDate(voucher.getVoucherDate());
        responseDTO.setCurrency(voucher.getCurrencyType());
        responseDTO.setObservations(voucher.getObservations());

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<VoucherDetailDTO> detailDTOs = new ArrayList<>();

        for (VoucherDetail detail : details) {
            VoucherDetailDTO detailDTO = new VoucherDetailDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setDetail(detail.getDetail());
            detailDTO.setUnitPrice(detail.getUnitPrice());
            detailDTO.setAmount(detail.getAmount());
            detailDTO.setDueDate(detail.getDueDate());
            detailDTOs.add(detailDTO);

            BigDecimal subtotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getAmount()));
            totalAmount = totalAmount.add(subtotal);
        }

        responseDTO.setDetails(detailDTOs);
        responseDTO.setTotalAmount(totalAmount);

        return responseDTO;
    }

    public List<VoucherResponseDTO> getVouchersByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("No se encontró el paciente con ID: " + patientId);
        }

        List<Voucher> vouchers = voucherRepository.findByPatientId(patientId);
        List<VoucherResponseDTO> responseList = new ArrayList<>();

        for (Voucher voucher : vouchers) {
            List<VoucherDetail> details = voucherDetailRepository.findByVoucherId(voucher.getId());

            BigDecimal totalAmount = BigDecimal.ZERO;

            for (VoucherDetail detail : details) {
                BigDecimal subtotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getAmount()));
                totalAmount = totalAmount.add(subtotal);
            }

            responseList.add(new VoucherResponseDTO(
                    voucher.getId(),
                    voucher.getVoucherDate(),
                    totalAmount,
                    voucher.getCurrencyType()
            ));
        }

        return responseList;
    }

    @Transactional
    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comprobante no encontrado con ID: " + id));

        Patient patient = voucher.getPatient();

        // 1. Buscar y eliminar en cascada todos los recibos asociados a este comprobante
        List<Receipt> associatedReceipts = receiptRepository.findByVoucherId(id);
        for (Receipt r : associatedReceipts) {
            receiptService.deleteReceipt(r.getId());
        }

        // 2. Eliminar los registros de Cuenta Corriente asociados directamente al comprobante
        List<CurrentAccount> accounts = currentAccountRepository.findByVoucherId(id);
        if (!accounts.isEmpty()) {
            currentAccountRepository.deleteAll(accounts);
        }

        // 3. Eliminar los detalles del comprobante
        List<VoucherDetail> details = voucherDetailRepository.findByVoucherId(id);
        if (!details.isEmpty()) {
            voucherDetailRepository.deleteAll(details);
        }

        // 4. Eliminar el comprobante
        voucherRepository.delete(voucher);

        // 5. Reconstruir los saldos del paciente y actualizar su estado final
        currentAccountService.rebuildPatientBalances(patient.getId());
        currentAccountService.updateDebtorLabel(patient);
    }
}