package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrentAccountType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrentAccountMovementDTO {
    private Long id;
    private LocalDate date;
    private CurrentAccountType type;
    private String detail;
    private BigDecimal transactionAmountPesos;
    private BigDecimal transactionAmountDollars;
    private BigDecimal balancePesos;
    private BigDecimal balanceDollars;
    private Boolean canceled;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public CurrentAccountType getType() { return type; }
    public void setType(CurrentAccountType type) { this.type = type; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public BigDecimal getTransactionAmountPesos() { return transactionAmountPesos; }
    public void setTransactionAmountPesos(BigDecimal transactionAmountPesos) { this.transactionAmountPesos = transactionAmountPesos; }

    public BigDecimal getTransactionAmountDollars() { return transactionAmountDollars; }
    public void setTransactionAmountDollars(BigDecimal transactionAmountDollars) { this.transactionAmountDollars = transactionAmountDollars; }

    public BigDecimal getBalancePesos() { return balancePesos; }
    public void setBalancePesos(BigDecimal balancePesos) { this.balancePesos = balancePesos; }

    public BigDecimal getBalanceDollars() { return balanceDollars; }
    public void setBalanceDollars(BigDecimal balanceDollars) { this.balanceDollars = balanceDollars; }

    public Boolean getCanceled() { return canceled; }
    public void setCanceled(Boolean canceled) { this.canceled = canceled; }
}