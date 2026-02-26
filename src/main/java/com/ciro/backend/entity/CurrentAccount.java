package com.ciro.backend.entity;

import com.ciro.backend.enums.CurrentAccountType;
import jakarta.persistence.*;
import lombok.*;

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
    private CurrentAccountType type; // VOUCHER o RECEIPT

    @Column
    private Boolean canceled;

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
}