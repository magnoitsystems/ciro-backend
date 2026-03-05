package com.ciro.backend.entity;

import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "current_account")
public class CurrentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_voucher", nullable = true)
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "id_receipt", nullable = true)
    private Receipt receipt;

    @Enumerated(EnumType.STRING)
    private CurrentAccountType type;

    @Column
    private Boolean canceled;

    @Column(name = "transaction_amount_pesos", precision = 12, scale = 2)
    private BigDecimal transactionAmountPesos;

    @Column(name = "transaction_amount_dollars", precision = 12, scale = 2)
    private BigDecimal transactionAmountDollars;

    @Column(name = "balance_pesos", precision = 12, scale = 2)
    private BigDecimal balancePesos;

    @Column(name = "balance_dollars", precision = 12, scale = 2)
    private BigDecimal balanceDollars;

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public CurrentAccountType getType() {
        return type;
    }

    public void setType(CurrentAccountType type) {
        this.type = type;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public BigDecimal getTransactionAmountPesos() { return transactionAmountPesos; }
    public void setTransactionAmountPesos(BigDecimal transactionAmountPesos) { this.transactionAmountPesos = transactionAmountPesos; }

    public BigDecimal getTransactionAmountDollars() { return transactionAmountDollars; }
    public void setTransactionAmountDollars(BigDecimal transactionAmountDollars) { this.transactionAmountDollars = transactionAmountDollars; }

    public BigDecimal getBalancePesos() { return balancePesos; }
    public void setBalancePesos(BigDecimal balancePesos) { this.balancePesos = balancePesos; }

    public BigDecimal getBalanceDollars() { return balanceDollars; }
    public void setBalanceDollars(BigDecimal balanceDollars) { this.balanceDollars = balanceDollars; }
}