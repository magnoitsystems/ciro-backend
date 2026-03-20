package com.ciro.backend.dto;

import java.math.BigDecimal;

public class FinancialStatisticsDTO {
    private BigDecimal totalIncomePesos;
    private BigDecimal totalIncomeDollars;
    private BigDecimal totalExpensesPesos;

    public BigDecimal getTotalIncomePesos() {
        return totalIncomePesos;
    }

    public void setTotalIncomePesos(BigDecimal totalIncomePesos) {
        this.totalIncomePesos = totalIncomePesos;
    }

    public BigDecimal getTotalIncomeDollars() {
        return totalIncomeDollars;
    }

    public void setTotalIncomeDollars(BigDecimal totalIncomeDollars) {
        this.totalIncomeDollars = totalIncomeDollars;
    }

    public BigDecimal getTotalExpensesPesos() {
        return totalExpensesPesos;
    }

    public void setTotalExpensesPesos(BigDecimal totalExpensesPesos) {
        this.totalExpensesPesos = totalExpensesPesos;
    }
}