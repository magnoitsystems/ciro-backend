package com.ciro.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getTariffDate() {
        return tariffDate;
    }

    public void setTariffDate(LocalDate tariffDate) {
        this.tariffDate = tariffDate;
    }

    public BigDecimal getAmountDollars() {
        return amountDollars;
    }

    public void setAmountDollars(BigDecimal amountDollars) {
        this.amountDollars = amountDollars;
    }

    public BigDecimal getAmountPesos() {
        return amountPesos;
    }

    public void setAmountPesos(BigDecimal amountPesos) {
        this.amountPesos = amountPesos;
    }

    public BigDecimal getTc() {
        return tc;
    }

    public void setTc(BigDecimal tc) {
        this.tc = tc;
    }
}