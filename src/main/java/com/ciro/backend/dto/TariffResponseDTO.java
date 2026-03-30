package com.ciro.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TariffResponseDTO {
    private Long id;
    private String name;
    private LocalDate tariffDate;
    private BigDecimal amountDollars;
    private BigDecimal amountPesos;
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