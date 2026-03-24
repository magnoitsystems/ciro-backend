package com.ciro.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class FinancialStatsDTO {
    private BigDecimal currentMonthIncomePesos;
    private BigDecimal currentMonthIncomeDollars;
    private BigDecimal currentMonthExpensesPesos;
    private BigDecimal currentMonthExpensesDollars;

    private BigDecimal previousMonthIncomePesos;
    private BigDecimal previousMonthIncomeDollars;

    private List<StatItemDTO> incomeBreakdown;

    public BigDecimal getCurrentMonthIncomePesos() {
        return currentMonthIncomePesos;
    }

    public void setCurrentMonthIncomePesos(BigDecimal currentMonthIncomePesos) {
        this.currentMonthIncomePesos = currentMonthIncomePesos;
    }

    public BigDecimal getCurrentMonthIncomeDollars() {
        return currentMonthIncomeDollars;
    }

    public void setCurrentMonthIncomeDollars(BigDecimal currentMonthIncomeDollars) {
        this.currentMonthIncomeDollars = currentMonthIncomeDollars;
    }

    public BigDecimal getCurrentMonthExpensesPesos() {
        return currentMonthExpensesPesos;
    }

    public void setCurrentMonthExpensesPesos(BigDecimal currentMonthExpensesPesos) {
        this.currentMonthExpensesPesos = currentMonthExpensesPesos;
    }

    public BigDecimal getCurrentMonthExpensesDollars() {
        return currentMonthExpensesDollars;
    }

    public void setCurrentMonthExpensesDollars(BigDecimal currentMonthExpensesDollars) {
        this.currentMonthExpensesDollars = currentMonthExpensesDollars;
    }

    public BigDecimal getPreviousMonthIncomePesos() {
        return previousMonthIncomePesos;
    }

    public void setPreviousMonthIncomePesos(BigDecimal previousMonthIncomePesos) {
        this.previousMonthIncomePesos = previousMonthIncomePesos;
    }

    public BigDecimal getPreviousMonthIncomeDollars() {
        return previousMonthIncomeDollars;
    }

    public void setPreviousMonthIncomeDollars(BigDecimal previousMonthIncomeDollars) {
        this.previousMonthIncomeDollars = previousMonthIncomeDollars;
    }

    public List<StatItemDTO> getIncomeBreakdown() {
        return incomeBreakdown;
    }

    public void setIncomeBreakdown(List<StatItemDTO> incomeBreakdown) {
        this.incomeBreakdown = incomeBreakdown;
    }
}