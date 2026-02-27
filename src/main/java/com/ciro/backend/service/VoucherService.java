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
import java.util.List;

@Service
public class VoucherService {

    @Autowired private VoucherRepository voucherRepository;
    @Autowired private VoucherDetailRepository voucherDetailRepository;
    @Autowired private CurrentAccountRepository currentAccountRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;

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
        voucher.setCurrency(dto.getCurrency());
        voucher.setObservations(dto.getObservations());

        Voucher savedVoucher = voucherRepository.save(voucher);

        BigDecimal totalCalculated = BigDecimal.ZERO;

        for (VoucherDetailDTO detailDto : dto.getDetails()) {
            VoucherDetail detail = new VoucherDetail();
            detail.setVoucher(savedVoucher);
            detail.setDetail(detailDto.getDetail());
            detail.setUnitPrice(detailDto.getUnitPrice());
            detail.setAmount(detailDto.getAmount());

            voucherDetailRepository.save(detail);

            BigDecimal subtotal = detailDto.getUnitPrice().multiply(BigDecimal.valueOf(detailDto.getAmount()));
            totalCalculated = totalCalculated.add(subtotal);
        }

        CurrentAccount accountEntry = new CurrentAccount();
        accountEntry.setPatient(patient);
        accountEntry.setVoucher(savedVoucher);
        accountEntry.setType(CurrentAccountType.VOUCHER);
        accountEntry.setCanceled(false);

        CurrencyType txCurrency = savedVoucher.getCurrency();
        accountEntry.setCurrency(txCurrency);

        BigDecimal previousBalance = currentAccountRepository
                .findTopByPatientIdAndCurrencyOrderByIdDesc(patient.getId(), txCurrency)
                .map(CurrentAccount::getBalance)
                .orElse(BigDecimal.ZERO);

        BigDecimal newBalance = previousBalance.add(totalCalculated);
        accountEntry.setBalance(newBalance);

        currentAccountRepository.save(accountEntry);

        return new VoucherResponseDTO(
                savedVoucher.getId(),
                savedVoucher.getVoucherDate(),
                savedVoucher.getCurrency(),
                totalCalculated
        );
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
        responseDTO.setCurrency(voucher.getCurrency());
        responseDTO.setObservations(voucher.getObservations());

        BigDecimal total = BigDecimal.ZERO;
        List<VoucherDetailDTO> detailDTOs = new java.util.ArrayList<>();

        for (VoucherDetail detail : details) {
            VoucherDetailDTO detailDTO = new VoucherDetailDTO();
            detailDTO.setDetail(detail.getDetail());
            detailDTO.setUnitPrice(detail.getUnitPrice());
            detailDTO.setAmount(detail.getAmount());

            detailDTOs.add(detailDTO);

            BigDecimal subtotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getAmount()));
            total = total.add(subtotal);
        }

        responseDTO.setDetails(detailDTOs);
        responseDTO.setTotalAmount(total);

        return responseDTO;
    }

    public List<VoucherResponseDTO> getVouchersByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("No se encontró el paciente con ID: " + patientId);
        }

        List<Voucher> vouchers = voucherRepository.findByPatientId(patientId);
        List<VoucherResponseDTO> responseList = new java.util.ArrayList<>();

        for (Voucher voucher : vouchers) {

            List<VoucherDetail> details = voucherDetailRepository.findByVoucherId(voucher.getId());
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (VoucherDetail detail : details) {
                BigDecimal subtotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getAmount()));
                totalAmount = totalAmount.add(subtotal);
            }

            VoucherResponseDTO dto = new VoucherResponseDTO(
                    voucher.getId(),
                    voucher.getVoucherDate(),
                    voucher.getCurrency(),
                    totalAmount
            );

            responseList.add(dto);
        }

        return responseList;
    }
}