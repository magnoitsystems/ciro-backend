package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class VoucherResponseDTO {
    private Long voucherId;
    private LocalDate date;
    private CurrencyType currency;
    private BigDecimal totalAmount;

    public VoucherResponseDTO(Long voucherId, LocalDate date, CurrencyType currency, BigDecimal totalAmount) {
        this.voucherId = voucherId;
        this.date = date;
        this.currency = currency;
        this.totalAmount = totalAmount;
    }

    public Long getVoucherId() { return voucherId; }
    public void setVoucherId(Long voucherId) { this.voucherId = voucherId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public CurrencyType getCurrency() { return currency; }
    public void setCurrency(CurrencyType currency) { this.currency = currency; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}