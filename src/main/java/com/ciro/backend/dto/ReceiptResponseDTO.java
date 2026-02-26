package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceiptResponseDTO {

    private Long id;
    private LocalDate receiptDate;
    private BigDecimal amount;
    private CurrencyType currencyType;

    public ReceiptResponseDTO(Long id,
                              LocalDate receiptDate,
                              BigDecimal amount,
                              CurrencyType currencyType) {
        this.id = id;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.currencyType = currencyType;
    }

    public Long getId() { return id; }
    public LocalDate getReceiptDate() { return receiptDate; }
    public BigDecimal getAmount() { return amount; }
    public CurrencyType getCurrencyType() { return currencyType; }
}