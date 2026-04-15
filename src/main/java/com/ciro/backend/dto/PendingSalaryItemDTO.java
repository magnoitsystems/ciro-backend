package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class PendingSalaryItemDTO {
    private Long id;
    private String employeeFullName;
    private BigDecimal amount;
    private CurrencyType currencyType;

    public PendingSalaryItemDTO(Long id, String employeeFullName, BigDecimal amount, CurrencyType currencyType) {
        this.id = id;
        this.employeeFullName = employeeFullName;
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

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
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