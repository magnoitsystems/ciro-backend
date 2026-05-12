package com.ciro.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class FinancialStatsDTO {
    private BigDecimal currentPeriodIncomePesos;
    private BigDecimal currentPeriodIncomeDollars;
    private BigDecimal currentPeriodExpensesPesos;
    private BigDecimal currentPeriodExpensesDollars;
    private BigDecimal netProfitPesos;
    private BigDecimal netProfitDollars;
    private List<StatItemDTO> incomeBreakdown;
    private List<StatItemDTO> expensesBreakdown;

    public BigDecimal getCurrentPeriodIncomePesos() {
        return currentPeriodIncomePesos;
    }

    public void setCurrentPeriodIncomePesos(BigDecimal currentPeriodIncomePesos) {
        this.currentPeriodIncomePesos = currentPeriodIncomePesos;
    }

    public BigDecimal getCurrentPeriodIncomeDollars() {
        return currentPeriodIncomeDollars;
    }

    public void setCurrentPeriodIncomeDollars(BigDecimal currentPeriodIncomeDollars) {
        this.currentPeriodIncomeDollars = currentPeriodIncomeDollars;
    }

    public BigDecimal getCurrentPeriodExpensesPesos() {
        return currentPeriodExpensesPesos;
    }

    public void setCurrentPeriodExpensesPesos(BigDecimal currentPeriodExpensesPesos) {
        this.currentPeriodExpensesPesos = currentPeriodExpensesPesos;
    }

    public BigDecimal getCurrentPeriodExpensesDollars() {
        return currentPeriodExpensesDollars;
    }

    public void setCurrentPeriodExpensesDollars(BigDecimal currentPeriodExpensesDollars) {
        this.currentPeriodExpensesDollars = currentPeriodExpensesDollars;
    }

    public BigDecimal getNetProfitPesos() {
        return netProfitPesos;
    }

    public void setNetProfitPesos(BigDecimal netProfitPesos) {
        this.netProfitPesos = netProfitPesos;
    }

    public BigDecimal getNetProfitDollars() {
        return netProfitDollars;
    }

    public void setNetProfitDollars(BigDecimal netProfitDollars) {
        this.netProfitDollars = netProfitDollars;
    }

    public List<StatItemDTO> getIncomeBreakdown() {
        return incomeBreakdown;
    }

    public void setIncomeBreakdown(List<StatItemDTO> incomeBreakdown) {
        this.incomeBreakdown = incomeBreakdown;
    }

    public List<StatItemDTO> getExpensesBreakdown() {
        return expensesBreakdown;
    }

    public void setExpensesBreakdown(List<StatItemDTO> expensesBreakdown) {
        this.expensesBreakdown = expensesBreakdown;
    }
}