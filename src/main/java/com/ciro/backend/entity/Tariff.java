package com.ciro.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tariffs")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tariff_date")
    private LocalDate tariffDate;

    @Column(name = "amount_dollars", precision = 10, scale = 2)
    private BigDecimal amountDollars;

    @Column(name = "amount_pesos", precision = 10, scale = 2)
    private BigDecimal amountPesos;

    @Column(precision = 10, scale = 2)
    private BigDecimal tc;
}