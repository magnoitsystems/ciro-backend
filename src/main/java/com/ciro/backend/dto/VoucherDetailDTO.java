package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;

import java.math.BigDecimal;

public class VoucherDetailDTO {
    private String detail;
    private BigDecimal unitPrice;
    private CurrencyType currency;
    private int amount;

    public CurrencyType getCurrency() {
        return currency;
    }
    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}