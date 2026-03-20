package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class VoucherResponseDTO {
    private Long voucherId;
    private LocalDate date;
    private BigDecimal totalAmount;
    private CurrencyType currencyType;

    public VoucherResponseDTO(Long voucherId, LocalDate date, BigDecimal totalAmount, CurrencyType currencyType) {
        this.voucherId = voucherId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.currencyType = currencyType;
    }

    public Long getVoucherId() { return voucherId; }
    public void setVoucherId(Long voucherId) { this.voucherId = voucherId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public CurrencyType getCurrencyType() { return currencyType; }
    public void setCurrencyType(CurrencyType currencyType) { this.currencyType = currencyType; }
}