package com.ciro.backend.entity;

import com.ciro.backend.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @Column
    private BigDecimal amount;

    @Column
    private String observations;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type")
    private CurrencyType currencyType;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}