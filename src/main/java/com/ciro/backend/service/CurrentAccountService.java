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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentAccountService {

    @Autowired private CurrentAccountRepository currentAccountRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private LabelRepository labelRepository;
    @Autowired private LabelPatientRepository labelPatientRepository;

    public CurrentAccountResponseDTO getPatientCurrentAccount(Long patientId, CurrentAccountType type) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        CurrentAccountResponseDTO response = new CurrentAccountResponseDTO();
        response.setPatientId(patient.getId());
        response.setPatientFullName(patient.getFullName());

        CurrentAccount lastRecord = currentAccountRepository.findTopByPatientIdOrderByIdDesc(patientId).orElse(null);

        BigDecimal currentPesos = BigDecimal.ZERO;
        BigDecimal currentDollars = BigDecimal.ZERO;

        if (lastRecord != null && (lastRecord.getCanceled() == null || !lastRecord.getCanceled())) {
            currentPesos = lastRecord.getBalancePesos() != null ? lastRecord.getBalancePesos() : BigDecimal.ZERO;
            currentDollars = lastRecord.getBalanceDollars() != null ? lastRecord.getBalanceDollars() : BigDecimal.ZERO;
        }

        response.setDebtInPesos(currentPesos.compareTo(BigDecimal.ZERO) > 0 ? currentPesos : BigDecimal.ZERO);
        response.setDebtInDollars(currentDollars.compareTo(BigDecimal.ZERO) > 0 ? currentDollars : BigDecimal.ZERO);

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

            mov.setCanceled(acc.getCanceled() != null ? acc.getCanceled() : false);

            mov.setTransactionAmountPesos(acc.getTransactionAmountPesos() != null ? acc.getTransactionAmountPesos() : BigDecimal.ZERO);
            mov.setTransactionAmountDollars(acc.getTransactionAmountDollars() != null ? acc.getTransactionAmountDollars() : BigDecimal.ZERO);
            mov.setBalancePesos(acc.getBalancePesos() != null ? acc.getBalancePesos() : BigDecimal.ZERO);
            mov.setBalanceDollars(acc.getBalanceDollars() != null ? acc.getBalanceDollars() : BigDecimal.ZERO);

            if (acc.getType() == CurrentAccountType.VOUCHER && acc.getVoucher() != null) {
                Voucher v = acc.getVoucher();
                mov.setDate(v.getVoucherDate());

                String obs = v.getObservations() != null ? v.getObservations() : "Sin observaciones";
                mov.setDetail("Comprobante #" + v.getId() + " - " + obs);

            } else if (acc.getType() == CurrentAccountType.RECEIPT && acc.getReceipt() != null) {
                Receipt r = acc.getReceipt();
                mov.setDate(r.getReceiptDate());

                if (r.getConvertedAmount() != null) {
                    mov.setDetail("Recibo #" + r.getId() + " - Pagó $" + r.getAmount() + " PESOS (Cot: " + r.getExchangeRate() + ") -> Convertido a U$D");
                } else {
                    mov.setDetail("Recibo #" + r.getId() + " - Pago recibido en " + r.getCurrencyType());
                }
            }

            movements.add(mov);
        }

        response.setMovements(movements);
        return response;
    }

    @Transactional
    public void cancelPatientDebt(Long patientId) {
        CurrentAccount lastRecord = currentAccountRepository.findTopByPatientIdOrderByIdDesc(patientId)
                .orElseThrow(() -> new BadRequestException("El paciente no tiene movimientos en su cuenta corriente."));

        if (lastRecord.getCanceled() != null && lastRecord.getCanceled()) {
            throw new BadRequestException("La deuda ya se encuentra cancelada.");
        }

        lastRecord.setCanceled(true);
        currentAccountRepository.save(lastRecord);

        updateDebtorLabel(lastRecord.getPatient());
    }

    public void updateDebtorLabel(Patient patient) {
        CurrentAccount lastRecord = currentAccountRepository.findTopByPatientIdOrderByIdDesc(patient.getId()).orElse(null);

        BigDecimal pesosBalance = BigDecimal.ZERO;
        BigDecimal dolaresBalance = BigDecimal.ZERO;

        if (lastRecord != null && (lastRecord.getCanceled() == null || !lastRecord.getCanceled())) {
            pesosBalance = lastRecord.getBalancePesos() != null ? lastRecord.getBalancePesos() : BigDecimal.ZERO;
            dolaresBalance = lastRecord.getBalanceDollars() != null ? lastRecord.getBalanceDollars() : BigDecimal.ZERO;
        }

        boolean hasDebt = pesosBalance.compareTo(BigDecimal.ZERO) > 0 || dolaresBalance.compareTo(BigDecimal.ZERO) > 0;

        Label debtorLabel = labelRepository.findByLabel("Deudor")
                .orElseGet(() -> {
                    Label newLabel = new Label();
                    newLabel.setLabel("Deudor");
                    return labelRepository.save(newLabel);
                });

        LabelPatient alreadyHasLabel = labelPatientRepository.existsByPatientIdAndLabelId(patient.getId(), debtorLabel.getId());

        if (hasDebt && alreadyHasLabel == null) {
            LabelPatient newLabelPatient = new LabelPatient();
            newLabelPatient.setPatient(patient);
            newLabelPatient.setLabel(debtorLabel);
            labelPatientRepository.save(newLabelPatient);
        }
        else if (!hasDebt && alreadyHasLabel != null) {
            labelPatientRepository.deleteByPatientAndLabel(patient, debtorLabel);
        }
    }
}