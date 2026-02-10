package com.ciro.backend.entity;

import com.ciro.backend.enums.CurrentAccountType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
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
}