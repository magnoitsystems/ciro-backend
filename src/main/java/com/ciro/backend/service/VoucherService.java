package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;
import org.jspecify.annotations.Nullable;
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

    @Transactional
    public VoucherResponseDTO createVoucher(VoucherCreateDTO dto) {

        if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
            throw new BadRequestException("El comprobante debe tener al menos un detalle/procedimiento.");
        }

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        User professional = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));

        Voucher voucher = new Voucher();
        voucher.setPatient(patient);
        voucher.setUser(professional);
        voucher.setVoucherDate(dto.getVoucherDate() != null ? dto.getVoucherDate() : LocalDate.now());
        voucher.setObservations(dto.getObservations());

        Voucher savedVoucher = voucherRepository.save(voucher);

        BigDecimal totalPesos = BigDecimal.ZERO;
        BigDecimal totalDollars = BigDecimal.ZERO;

        for (VoucherDetailDTO detailDto : dto.getDetails()) {
            if (detailDto.getCurrency() == null) {
                throw new BadRequestException("Cada detalle debe especificar su moneda (currency).");
            }

            VoucherDetail detail = new VoucherDetail();
            detail.setVoucher(savedVoucher);
            detail.setDetail(detailDto.getDetail());
            detail.setUnitPrice(detailDto.getUnitPrice());
            detail.setAmount(detailDto.getAmount());
            detail.setCurrency(detailDto.getCurrency());

            voucherDetailRepository.save(detail);

            BigDecimal subtotal = detailDto.getUnitPrice().multiply(BigDecimal.valueOf(detailDto.getAmount()));

            if (detailDto.getCurrency() == CurrencyType.PESOS) {
                totalPesos = totalPesos.add(subtotal);
            } else if (detailDto.getCurrency() == CurrencyType.DOLARES) {
                totalDollars = totalDollars.add(subtotal);
            }
        }

        if (totalPesos.compareTo(BigDecimal.ZERO) > 0 || totalDollars.compareTo(BigDecimal.ZERO) > 0) {

            CurrentAccount accountEntry = new CurrentAccount();
            accountEntry.setPatient(patient);
            accountEntry.setVoucher(savedVoucher);
            accountEntry.setType(CurrentAccountType.VOUCHER);
            accountEntry.setCanceled(false);

            accountEntry.setTransactionAmountPesos(totalPesos);
            accountEntry.setTransactionAmountDollars(totalDollars);

            CurrentAccount lastRecord = currentAccountRepository.findTopByPatientIdOrderByIdDesc(patient.getId()).orElse(null);

            BigDecimal prevBalancePesos = (lastRecord != null && lastRecord.getBalancePesos() != null)
                    ? lastRecord.getBalancePesos() : BigDecimal.ZERO;
            BigDecimal prevBalanceDollars = (lastRecord != null && lastRecord.getBalanceDollars() != null)
                    ? lastRecord.getBalanceDollars() : BigDecimal.ZERO;

            accountEntry.setBalancePesos(prevBalancePesos.add(totalPesos));
            accountEntry.setBalanceDollars(prevBalanceDollars.add(totalDollars));

            currentAccountRepository.save(accountEntry);
        }

        currentAccountService.updateDebtorLabel(patient);

        return new VoucherResponseDTO(savedVoucher.getId(), savedVoucher.getVoucherDate(), totalPesos, totalDollars);
    }

    public VoucherDTO getVoucherById(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el comprobante con ID: " + id));

        List<VoucherDetail> details = voucherDetailRepository.findByVoucherId(id);

        VoucherDTO responseDTO = new VoucherDTO();
        responseDTO.setId(voucher.getId());
        responseDTO.setPatientFullName(voucher.getPatient().getFullName());
        responseDTO.setProfessionalFullName(voucher.getUser().getName() + " " + voucher.getUser().getLastname());
        responseDTO.setVoucherDate(voucher.getVoucherDate());
        responseDTO.setObservations(voucher.getObservations());

        BigDecimal totalPesos = BigDecimal.ZERO;
        BigDecimal totalDollars = BigDecimal.ZERO;
        List<VoucherDetailDTO> detailDTOs = new ArrayList<>();

        for (VoucherDetail detail : details) {
            VoucherDetailDTO detailDTO = new VoucherDetailDTO();
            detailDTO.setDetail(detail.getDetail());
            detailDTO.setUnitPrice(detail.getUnitPrice());
            detailDTO.setAmount(detail.getAmount());
            detailDTO.setCurrency(detail.getCurrency());

            detailDTOs.add(detailDTO);

            BigDecimal subtotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getAmount()));
            if (detail.getCurrency() == CurrencyType.PESOS) {
                totalPesos = totalPesos.add(subtotal);
            } else {
                totalDollars = totalDollars.add(subtotal);
            }
        }

        responseDTO.setDetails(detailDTOs);
        responseDTO.setTotalAmountPesos(totalPesos);
        responseDTO.setTotalAmountDollars(totalDollars);

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

            BigDecimal totalPesos = BigDecimal.ZERO;
            BigDecimal totalDollars = BigDecimal.ZERO;

            for (VoucherDetail detail : details) {
                BigDecimal subtotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getAmount()));
                if (detail.getCurrency() == CurrencyType.PESOS) {
                    totalPesos = totalPesos.add(subtotal);
                } else {
                    totalDollars = totalDollars.add(subtotal);
                }
            }

            responseList.add(new VoucherResponseDTO(
                    voucher.getId(),
                    voucher.getVoucherDate(),
                    totalPesos,
                    totalDollars
            ));
        }

        return responseList;
    }
}