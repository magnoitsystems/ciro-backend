package com.ciro.backend.service;

import com.ciro.backend.dto.ReceiptCreateDTO;
import com.ciro.backend.dto.ReceiptResponseDTO;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.CashMovementType;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReceiptService {

    @Autowired private ReceiptRepository receiptRepository;
    @Autowired private CurrentAccountRepository currentAccountRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CurrentAccountService currentAccountService;
    @Autowired private CashMovementService cashMovementService;
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private VoucherDetailRepository voucherDetailRepository;

    @Transactional
    public ReceiptResponseDTO createReceipt(ReceiptCreateDTO dto) {

        if (dto.getAmount() == null || dto.getAmount().doubleValue() <= 0) {
            throw new BadRequestException("El importe del recibo debe ser mayor a 0.");
        }

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        User doctor = null;
        if (dto.getDoctorId() != null) {
            doctor = userRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));
        }

        BigDecimal convertedAmount = null;

        boolean isPesoToDollarConversion = (dto.getCurrencyType() == CurrencyType.PESOS
                && dto.isPayDollarDebtWithPesos()
                && dto.getExchangeRate() != null
                && dto.getExchangeRate().compareTo(BigDecimal.ZERO) > 0);

        if (isPesoToDollarConversion) {
            convertedAmount = dto.getAmount().divide(dto.getExchangeRate(), 2, RoundingMode.HALF_UP);
        } else if (dto.isPayDollarDebtWithPesos() && (dto.getExchangeRate() == null || dto.getExchangeRate().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BadRequestException("Debe ingresar la cotización (Exchange Rate) para saldar deuda en dólares con pesos.");
        }

        Receipt receipt = new Receipt();
        receipt.setReceiptDate(dto.getReceiptDate() != null ? dto.getReceiptDate() : LocalDate.now());
        receipt.setAmount(dto.getAmount());
        receipt.setObservations(dto.getObservations());
        receipt.setCurrencyType(dto.getCurrencyType());
        receipt.setExchangeRate(dto.getExchangeRate());
        receipt.setConvertedAmount(convertedAmount);
        receipt.setPatient(patient);
        receipt.setDoctor(doctor);
        receipt.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(dto.getVoucherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comprobante no encontrado"));
            receipt.setVoucher(voucher);
        }

        if (dto.getVoucherDetailId() != null) {
            VoucherDetail voucherDetail = voucherDetailRepository.findById(dto.getVoucherDetailId())
                    .orElseThrow(() -> new ResourceNotFoundException("Detalle de comprobante no encontrado"));
            receipt.setVoucherDetail(voucherDetail);
        }

        Receipt savedReceipt = receiptRepository.save(receipt);

        CurrentAccount accountEntry = new CurrentAccount();
        accountEntry.setPatient(patient);
        accountEntry.setReceipt(savedReceipt);
        accountEntry.setType(CurrentAccountType.RECEIPT);
        accountEntry.setCanceled(false);

        CurrentAccount lastRecord = currentAccountRepository.findTopByPatientIdOrderByIdDesc(patient.getId()).orElse(null);

        BigDecimal prevBalancePesos = BigDecimal.ZERO;
        BigDecimal prevBalanceDollars = BigDecimal.ZERO;

        if (lastRecord != null && (lastRecord.getCanceled() == null || !lastRecord.getCanceled())) {
            prevBalancePesos = lastRecord.getBalancePesos() != null ? lastRecord.getBalancePesos() : BigDecimal.ZERO;
            prevBalanceDollars = lastRecord.getBalanceDollars() != null ? lastRecord.getBalanceDollars() : BigDecimal.ZERO;
        }

        BigDecimal txPesos = BigDecimal.ZERO;
        BigDecimal txDollars = BigDecimal.ZERO;
        BigDecimal newBalancePesos = prevBalancePesos;
        BigDecimal newBalanceDollars = prevBalanceDollars;

        if (isPesoToDollarConversion) {
            txDollars = convertedAmount;
            txPesos = dto.getAmount();
            newBalanceDollars = prevBalanceDollars.subtract(convertedAmount);
        } else {
            if (dto.getCurrencyType() == CurrencyType.PESOS) {
                txPesos = dto.getAmount();
                newBalancePesos = prevBalancePesos.subtract(dto.getAmount());
            } else if (dto.getCurrencyType() == CurrencyType.DOLARES) {
                txDollars = dto.getAmount();
                newBalanceDollars = prevBalanceDollars.subtract(dto.getAmount());
            }
        }

        accountEntry.setTransactionAmountPesos(txPesos);
        accountEntry.setTransactionAmountDollars(txDollars);

        accountEntry.setBalancePesos(newBalancePesos);
        accountEntry.setBalanceDollars(newBalanceDollars);

        currentAccountRepository.save(accountEntry);
        currentAccountService.updateDebtorLabel(patient);

        cashMovementService.createMovement(
                savedReceipt.getAmount(),
                savedReceipt.getCurrencyType(),
                savedReceipt.getPaymentMethod(),
                savedReceipt.getId(),
                CashMovementType.INGRESO,
                "Ingreso por recibo de paciente: " + patient.getFullName(),
                savedReceipt.getDoctor() != null ? savedReceipt.getDoctor().getId() : null
        );

        return mapToDTO(savedReceipt);
    }

    @Transactional
    public ReceiptResponseDTO updateReceipt(Long id, ReceiptCreateDTO dto) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo no encontrado"));

        receipt.setAmount(dto.getAmount());
        receipt.setObservations(dto.getObservations());
        receipt.setReceiptDate(dto.getReceiptDate());
        receipt.setPaymentMethod(dto.getPaymentMethod());
        receipt.setCurrencyType(dto.getCurrencyType());
        receipt.setExchangeRate(dto.getExchangeRate());

        if (dto.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(dto.getVoucherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comprobante no encontrado"));
            receipt.setVoucher(voucher);
        } else {
            receipt.setVoucher(null);
        }

        if (dto.getVoucherDetailId() != null) {
            VoucherDetail voucherDetail = voucherDetailRepository.findById(dto.getVoucherDetailId())
                    .orElseThrow(() -> new ResourceNotFoundException("Detalle de comprobante no encontrado"));
            receipt.setVoucherDetail(voucherDetail);
        } else {
            receipt.setVoucherDetail(null);
        }

        Receipt updatedReceipt = receiptRepository.save(receipt);
        currentAccountService.rebuildPatientBalances(updatedReceipt.getPatient().getId());

        return mapToDTO(updatedReceipt);
    }

    public List<ReceiptResponseDTO> getReceiptsByPatient(Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente no encontrado");
        }

        List<Receipt> receipts = receiptRepository.findByPatientIdOrderByReceiptDateDesc(patientId);

        return receipts.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ReceiptResponseDTO getReceiptById(Long id) {

        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo no encontrado"));

        return mapToDTO(receipt);
    }

    private ReceiptResponseDTO mapToDTO(Receipt r) {
        ReceiptResponseDTO dto = new ReceiptResponseDTO(
                r.getId(),
                r.getReceiptDate(),
                r.getAmount(),
                r.getCurrencyType(),
                r.getExchangeRate(),
                r.getConvertedAmount(),
                r.getPatient() != null ? r.getPatient().getFullName() : null,
                r.getPatient() != null ? r.getPatient().getDni() : null,
                r.getDoctor() != null ? r.getDoctor().getName() + " " + r.getDoctor().getLastname() : null,
                r.getPaymentMethod(),
                r.getObservations(),
                r.getVoucher() != null ? r.getVoucher().getId() : null,
                r.getVoucherDetail() != null ? r.getVoucherDetail().getId() : null
        );
        return dto;
    }
}