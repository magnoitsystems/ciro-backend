package com.ciro.backend.dto;

import java.math.BigDecimal;

public class RevenueWidgetDTO {
    private BigDecimal totalPesos;
    private BigDecimal totalDollars;

    public RevenueWidgetDTO(BigDecimal totalPesos, BigDecimal totalDollars) {
        this.totalPesos = totalPesos;
        this.totalDollars = totalDollars;
    }

    public BigDecimal getTotalPesos() { return totalPesos; }
    public void setTotalPesos(BigDecimal totalPesos) { this.totalPesos = totalPesos; }
    public BigDecimal getTotalDollars() { return totalDollars; }
    public void setTotalDollars(BigDecimal totalDollars) { this.totalDollars = totalDollars; }
}