package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.enums.PaymentMethod;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CurrentAccountService {

    @Autowired private CurrentAccountRepository currentAccountRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private LabelRepository labelRepository;
    @Autowired private LabelPatientRepository labelPatientRepository;
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private VoucherDetailRepository voucherDetailRepository;
    @Autowired private ReceiptRepository receiptRepository;

    public PatientDebtInfo calculateDebtAndOverdue(Long patientId) {
        List<Voucher> vouchers = voucherRepository.findByPatientId(patientId);
        List<Receipt> receiptsDesc = receiptRepository.findByPatientIdOrderByReceiptDateDesc(patientId);
        List<Receipt> receipts = new ArrayList<>(receiptsDesc);
        Collections.reverse(receipts);

        BigDecimal debtPesos = BigDecimal.ZERO;
        BigDecimal debtDolares = BigDecimal.ZERO;
        boolean isOverdue = false;

        class DetailRem {
            VoucherDetail detail;
            BigDecimal remaining;
            CurrencyType currency;
            DetailRem(VoucherDetail d, CurrencyType c) {
                detail = d;
                remaining = d.getUnitPrice().multiply(new BigDecimal(d.getAmount()));
                currency = c;
            }
        }

        List<DetailRem> pendingDetails = new ArrayList<>();
        for (Voucher v : vouchers) {
            List<VoucherDetail> vDetails = voucherDetailRepository.findByVoucherId(v.getId());
            for (VoucherDetail vd : vDetails) {
                pendingDetails.add(new DetailRem(vd, v.getCurrencyType()));
            }
        }

        pendingDetails.sort((a, b) -> {
            LocalDate d1 = a.detail.getDueDate() != null ? a.detail.getDueDate() : LocalDate.MAX;
            LocalDate d2 = b.detail.getDueDate() != null ? b.detail.getDueDate() : LocalDate.MAX;
            return d1.compareTo(d2);
        });

        for (Receipt r : receipts) {
            BigDecimal amountToApply = r.getAmount();
            CurrencyType rCurrency = r.getCurrencyType();

            if (r.getCurrencyType() == CurrencyType.PESOS && r.getConvertedAmount() != null && r.getConvertedAmount().compareTo(BigDecimal.ZERO) > 0) {
                amountToApply = r.getConvertedAmount();
                rCurrency = CurrencyType.DOLARES;
            }

            if (r.getVoucherDetail() != null) {
                for (DetailRem dr : pendingDetails) {
                    if (dr.detail.getId().equals(r.getVoucherDetail().getId())) {
                        BigDecimal applied = amountToApply.min(dr.remaining);
                        dr.remaining = dr.remaining.subtract(applied);
                        amountToApply = amountToApply.subtract(applied);
                        break;
                    }
                }
            }

            if (amountToApply.compareTo(BigDecimal.ZERO) > 0 && r.getVoucher() != null) {
                for (DetailRem dr : pendingDetails) {
                    if (dr.remaining.compareTo(BigDecimal.ZERO) > 0 && dr.detail.getVoucher().getId().equals(r.getVoucher().getId())) {
                        BigDecimal applied = amountToApply.min(dr.remaining);
                        dr.remaining = dr.remaining.subtract(applied);
                        amountToApply = amountToApply.subtract(applied);
                        if (amountToApply.compareTo(BigDecimal.ZERO) <= 0) break;
                    }
                }
            }

            if (amountToApply.compareTo(BigDecimal.ZERO) > 0) {
                for (DetailRem dr : pendingDetails) {
                    if (dr.currency == rCurrency && dr.remaining.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal applied = amountToApply.min(dr.remaining);
                        dr.remaining = dr.remaining.subtract(applied);
                        amountToApply = amountToApply.subtract(applied);
                        if (amountToApply.compareTo(BigDecimal.ZERO) <= 0) break;
                    }
                }
            }
        }

        LocalDate today = LocalDate.now();
        List<VoucherDetail> unpaidDetails = new ArrayList<>();

        for (DetailRem dr : pendingDetails) {
            if (dr.remaining.compareTo(BigDecimal.ZERO) > 0) {
                unpaidDetails.add(dr.detail);
                if (dr.currency == CurrencyType.PESOS) {
                    debtPesos = debtPesos.add(dr.remaining);
                } else {
                    debtDolares = debtDolares.add(dr.remaining);
                }

                // SI DEBE DINERO Y LA FECHA LÍMITE YA PASÓ = ATRASADO
                if (dr.detail.getDueDate() != null && dr.detail.getDueDate().isBefore(today)) {
                    isOverdue = true;
                }
            }
        }

        return new PatientDebtInfo(debtPesos, debtDolares, isOverdue, unpaidDetails);
    }

    public CurrentAccountResponseDTO getPatientCurrentAccount(Long patientId, CurrentAccountType type) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        PatientDebtInfo debtInfo = calculateDebtAndOverdue(patientId);

        CurrentAccountResponseDTO response = new CurrentAccountResponseDTO();
        response.setPatientId(patient.getId());
        response.setPatientFullName(patient.getFullName());
        response.setDebtInPesos(debtInfo.getDebtPesos());
        response.setDebtInDollars(debtInfo.getDebtDolares());

        List<CurrentAccount> accounts = currentAccountRepository.findByPatientId(patientId);
        // Ordenar DESC para mostrar el más nuevo arriba
        accounts.sort((a, b) -> b.getId().compareTo(a.getId()));

        if (type != null) {
            accounts.removeIf(acc -> acc.getType() != type);
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

            if (acc.getReceipt() != null) mov.setReceiptId(acc.getReceipt().getId());
            if (acc.getVoucher() != null) mov.setVoucherId(acc.getVoucher().getId());

            if (acc.getType() == CurrentAccountType.VOUCHER && acc.getVoucher() != null) {
                Voucher v = acc.getVoucher();
                mov.setDate(v.getVoucherDate());
                String obs = v.getObservations() != null ? v.getObservations() : "Sin observaciones";
                mov.setDetail("Comprobante #" + v.getId() + " - " + obs + " (" + v.getCurrencyType() + ")");

            } else if (acc.getType() == CurrentAccountType.RECEIPT && acc.getReceipt() != null) {
                Receipt r = acc.getReceipt();
                mov.setDate(r.getReceiptDate());

                String associatedText = r.getVoucherDetail() != null ? " (Pago de Detalle #" + r.getVoucherDetail().getId() + ")" :
                        (r.getVoucher() != null ? " (Pago de Comprobante #" + r.getVoucher().getId() + ")" : "");

                if (r.getConvertedAmount() != null && r.getConvertedAmount().compareTo(BigDecimal.ZERO) > 0) {
                    mov.setDetail("Recibo #" + r.getId() + associatedText + " - Pagó $" + r.getAmount() + " PESOS (Cot: $" + r.getExchangeRate() + ") para saldar U$D " + r.getConvertedAmount());
                } else {
                    mov.setDetail("Recibo #" + r.getId() + associatedText + " - Pago de " + r.getCurrencyType());
                }
            } else if (acc.getCanceled() != null && acc.getCanceled()) {
                mov.setDate(LocalDate.now());
                mov.setDetail("Ajuste Manual / Cancelación de Deuda");
            }
            movements.add(mov);
        }
        response.setMovements(movements);
        return response;
    }

    @Transactional
    public void rebuildPatientBalances(Long patientId) {
        List<CurrentAccount> accounts = currentAccountRepository.findByPatientId(patientId);
        accounts.sort((a, b) -> a.getId().compareTo(b.getId()));

        BigDecimal bPesos = BigDecimal.ZERO;
        BigDecimal bDollars = BigDecimal.ZERO;

        for (CurrentAccount acc : accounts) {

            if (acc.getCanceled() != null && acc.getCanceled()) {
                acc.setTransactionAmountPesos(BigDecimal.ZERO);
                acc.setTransactionAmountDollars(BigDecimal.ZERO);
                acc.setBalancePesos(bPesos);
                acc.setBalanceDollars(bDollars);
                currentAccountRepository.save(acc);
                continue;
            }

            BigDecimal txPesos = BigDecimal.ZERO;
            BigDecimal txDollars = BigDecimal.ZERO;

            if (acc.getType() == CurrentAccountType.VOUCHER && acc.getVoucher() != null) {
                Voucher v = acc.getVoucher();
                if (v.getCurrencyType() == CurrencyType.PESOS) {
                    txPesos = v.getTotal_amount();
                    bPesos = bPesos.add(txPesos);
                } else {
                    txDollars = v.getTotal_amount();
                    bDollars = bDollars.add(txDollars);
                }
            }
            else if (acc.getType() == CurrentAccountType.RECEIPT && acc.getReceipt() != null) {
                Receipt r = acc.getReceipt();

                if (r.getCurrencyType() == CurrencyType.PESOS && r.getConvertedAmount() != null && r.getConvertedAmount().compareTo(BigDecimal.ZERO) > 0) {
                    txPesos = r.getAmount();
                    txDollars = r.getConvertedAmount();
                    bDollars = bDollars.subtract(txDollars);
                }
                else {
                    if (r.getCurrencyType() == CurrencyType.PESOS) {
                        txPesos = r.getAmount();
                        bPesos = bPesos.subtract(txPesos);
                    } else {
                        txDollars = r.getAmount();
                        bDollars = bDollars.subtract(txDollars);
                    }
                }
            }

            acc.setTransactionAmountPesos(txPesos);
            acc.setTransactionAmountDollars(txDollars);

            acc.setBalancePesos(bPesos);
            acc.setBalanceDollars(bDollars);

            currentAccountRepository.save(acc);
        }
    }

    @Transactional
    public void cancelVoucherDebt(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Comprobante no encontrado con ID: " + voucherId));

        Patient patient = voucher.getPatient();

        Receipt r = new Receipt();
        r.setPatient(patient);
        r.setVoucher(voucher);
        r.setAmount(voucher.getTotal_amount());
        r.setCurrencyType(voucher.getCurrencyType());
        r.setReceiptDate(LocalDate.now());
        r.setObservations("Cancelación administrativa del comprobante #" + voucherId);
        r.setPaymentMethod(PaymentMethod.EFECTIVO);

        Receipt savedR = receiptRepository.save(r);

        CurrentAccount ca = new CurrentAccount();
        ca.setPatient(patient);
        ca.setReceipt(savedR);
        ca.setType(CurrentAccountType.RECEIPT);

        ca.setCanceled(false);

        currentAccountRepository.save(ca);

        rebuildPatientBalances(patient.getId());
        updateDebtorLabel(patient);
    }

    @Transactional
    public void updateDebtorLabel(Patient patient) {
        PatientDebtInfo info = calculateDebtAndOverdue(patient.getId());
        boolean hasDebt = info.getDebtPesos().compareTo(BigDecimal.ZERO) > 0 || info.getDebtDolares().compareTo(BigDecimal.ZERO) > 0;

        Label debtorLabel = labelRepository.findByLabel("Deudor").orElseGet(() -> {
            Label newLabel = new Label();
            newLabel.setLabel("Deudor");
            return labelRepository.save(newLabel);
        });

        LabelPatient existingRelation = labelPatientRepository.findByPatientIdAndLabelId(patient.getId(), debtorLabel.getId()).orElse(null);
        boolean alreadyHasLabel = (existingRelation != null);

        if (hasDebt && !alreadyHasLabel) {
            LabelPatient newLabelPatient = new LabelPatient();
            newLabelPatient.setPatient(patient);
            newLabelPatient.setLabel(debtorLabel);
            labelPatientRepository.save(newLabelPatient);
        }
        else if (!hasDebt && alreadyHasLabel) {
            labelPatientRepository.delete(existingRelation);
        }
    }
}