package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentAccountService {

    @Autowired private CurrentAccountRepository currentAccountRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private VoucherDetailRepository voucherDetailRepository;

    public CurrentAccountResponseDTO getPatientCurrentAccount(Long patientId, CurrentAccountType type) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        CurrentAccountResponseDTO response = new CurrentAccountResponseDTO();
        response.setPatientId(patient.getId());
        response.setPatientFullName(patient.getFullName());

        BigDecimal latestPesosBalance = currentAccountRepository
                .findTopByPatientIdAndCurrencyOrderByIdDesc(patientId, CurrencyType.PESOS)
                .map(CurrentAccount::getBalance)
                .orElse(BigDecimal.ZERO);

        response.setDebtInPesos(latestPesosBalance.compareTo(BigDecimal.ZERO) > 0 ? latestPesosBalance : BigDecimal.ZERO);

        BigDecimal latestDollarsBalance = currentAccountRepository
                .findTopByPatientIdAndCurrencyOrderByIdDesc(patientId, CurrencyType.DOLARES)
                .map(CurrentAccount::getBalance)
                .orElse(BigDecimal.ZERO);

        response.setDebtInDollars(latestDollarsBalance.compareTo(BigDecimal.ZERO) > 0 ? latestDollarsBalance : BigDecimal.ZERO);

        List<CurrentAccount> accounts;
        if (type != null) {
            accounts = currentAccountRepository.findByPatientIdAndTypeOrderByIdDesc(patientId, type);
        } else {
            accounts = currentAccountRepository.findByPatientIdOrderByIdDesc(patientId);
        }

        List<CurrentAccountMovementDTO> movements = new ArrayList<>();

        for (CurrentAccount acc : accounts) {
            CurrentAccountMovementDTO mov = new CurrentAccountMovementDTO();
            mov.setId(acc.getId());
            mov.setType(acc.getType());
            mov.setCurrency(acc.getCurrency());
            mov.setBalance(acc.getBalance());

            if (acc.getType() == CurrentAccountType.VOUCHER && acc.getVoucher() != null) {
                Voucher v = acc.getVoucher();
                mov.setDate(v.getVoucherDate());
                mov.setDetail("Comprobante #" + v.getId() + " - " + v.getObservations());

                List<VoucherDetail> details = voucherDetailRepository.findByVoucherId(v.getId());
                BigDecimal voucherTotal = BigDecimal.ZERO;
                for (VoucherDetail d : details) {
                    voucherTotal = voucherTotal.add(d.getUnitPrice().multiply(BigDecimal.valueOf(d.getAmount())));
                }
                mov.setTransactionAmount(voucherTotal);

            } else if (acc.getType() == CurrentAccountType.RECEIPT && acc.getReceipt() != null) {
                Receipt r = acc.getReceipt();
                mov.setDate(r.getReceiptDate());

                if (r.getConvertedAmount() != null) {
                    mov.setDetail("Recibo #" + r.getId() + " - Pagó $" + r.getAmount() + " PESOS (Cot: " + r.getExchangeRate() + ") -> Convertido a U$D");
                    mov.setTransactionAmount(r.getConvertedAmount());
                } else {
                    mov.setDetail("Recibo #" + r.getId() + " - Pago recibido en " + r.getCurrencyType());
                    mov.setTransactionAmount(r.getAmount());
                }
            }

            movements.add(mov);
        }

        response.setMovements(movements);
        return response;
    }
}