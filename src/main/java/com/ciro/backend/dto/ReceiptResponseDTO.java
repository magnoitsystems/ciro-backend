package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceiptResponseDTO {

    private Long id;
    private LocalDate receiptDate;
    private BigDecimal amount;
    private CurrencyType currencyType;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;

    // ¡NUEVOS CAMPOS!
    private String patientFullName;
    private String patientDni;

    public ReceiptResponseDTO(Long id,
                              LocalDate receiptDate,
                              BigDecimal amount,
                              CurrencyType currencyType,
                              BigDecimal exchangeRate,
                              BigDecimal convertedAmount,
                              String patientFullName,
                              String patientDni) {
        this.id = id;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.currencyType = currencyType;
        this.exchangeRate = exchangeRate;
        this.convertedAmount = convertedAmount;
        this.patientFullName = patientFullName;
        this.patientDni = patientDni;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDate getReceiptDate() { return receiptDate; }
    public BigDecimal getAmount() { return amount; }
    public CurrencyType getCurrencyType() { return currencyType; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public String getPatientFullName() { return patientFullName; }
    public String getPatientDni() { return patientDni; }
}