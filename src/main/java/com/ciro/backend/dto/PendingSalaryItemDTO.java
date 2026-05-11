package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class PendingSalaryItemDTO {
    private Long id;
    private String fullName;
    private BigDecimal amount;
    private CurrencyType currencyType;

    public PendingSalaryItemDTO(Long id, String fullName, BigDecimal amount, CurrencyType currencyType) {
        this.id = id;
        this.fullName = fullName;
        this.amount = amount;
        this.currencyType = currencyType;
    }

    public PendingSalaryItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String employeeFullName) {
        this.fullName = employeeFullName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }
}