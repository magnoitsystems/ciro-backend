package com.ciro.backend.entity;

import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
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

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyType currency;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;
}