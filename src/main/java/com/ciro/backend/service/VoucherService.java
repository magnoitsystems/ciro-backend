package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

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

        currentAccountRepository.save(accountEntry);

        return new VoucherResponseDTO(
                savedVoucher.getId(),
                savedVoucher.getVoucherDate(),
                savedVoucher.getCurrency(),
                totalCalculated
        );
    }
}