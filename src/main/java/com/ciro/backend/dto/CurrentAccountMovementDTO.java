package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrentAccountMovementDTO {
    private Long id;
    private LocalDate date;
    private CurrentAccountType type;
    private String detail;
    private BigDecimal transactionAmount;
    private CurrencyType currency;
    private BigDecimal balance;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public CurrentAccountType getType() { return type; }
    public void setType(CurrentAccountType type) { this.type = type; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public BigDecimal getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(BigDecimal transactionAmount) { this.transactionAmount = transactionAmount; }
    public CurrencyType getCurrency() { return currency; }
    public void setCurrency(CurrencyType currency) { this.currency = currency; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}