package com.ciro.backend.dto;

import java.math.BigDecimal;

public class CashBalanceDTO {
    private BigDecimal balancePesos;
    private BigDecimal balanceDolares;
    private BigDecimal totalIngresosPesos;
    private BigDecimal totalEgresosPesos;
    private BigDecimal totalIngresosDolares;
    private BigDecimal totalEgresosDolares;

    public CashBalanceDTO(BigDecimal balancePesos, BigDecimal balanceDolares,
                          BigDecimal totalIngresosPesos, BigDecimal totalEgresosPesos,
                          BigDecimal totalIngresosDolares, BigDecimal totalEgresosDolares) {
        this.balancePesos = balancePesos;
        this.balanceDolares = balanceDolares;
        this.totalIngresosPesos = totalIngresosPesos;
        this.totalEgresosPesos = totalEgresosPesos;
        this.totalIngresosDolares = totalIngresosDolares;
        this.totalEgresosDolares = totalEgresosDolares;
    }

    public BigDecimal getBalancePesos() { return balancePesos; }
    public void setBalancePesos(BigDecimal balancePesos) { this.balancePesos = balancePesos; }

    public BigDecimal getBalanceDolares() { return balanceDolares; }
    public void setBalanceDolares(BigDecimal balanceDolares) { this.balanceDolares = balanceDolares; }

    public BigDecimal getTotalIngresosPesos() { return totalIngresosPesos; }
    public void setTotalIngresosPesos(BigDecimal totalIngresosPesos) { this.totalIngresosPesos = totalIngresosPesos; }

    public BigDecimal getTotalEgresosPesos() { return totalEgresosPesos; }
    public void setTotalEgresosPesos(BigDecimal totalEgresosPesos) { this.totalEgresosPesos = totalEgresosPesos; }

    public BigDecimal getTotalIngresosDolares() { return totalIngresosDolares; }
    public void setTotalIngresosDolares(BigDecimal totalIngresosDolares) { this.totalIngresosDolares = totalIngresosDolares; }

    public BigDecimal getTotalEgresosDolares() { return totalEgresosDolares; }
    public void setTotalEgresosDolares(BigDecimal totalEgresosDolares) { this.totalEgresosDolares = totalEgresosDolares; }
}