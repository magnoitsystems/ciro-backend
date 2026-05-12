package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceiptResponseDTO {

    private Long id;
    private LocalDate receiptDate;
    private BigDecimal amount;
    private CurrencyType currencyType;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
    private String patientFullName;
    private String patientDni;
    private String doctorFullName;
    private PaymentMethod paymentMethod;
    private String observations;
    private Long voucherId;
    private Long voucherDetailId;


    public ReceiptResponseDTO(Long id,
                              LocalDate receiptDate,
                              BigDecimal amount,
                              CurrencyType currencyType,
                              BigDecimal exchangeRate,
                              BigDecimal convertedAmount,
                              String patientFullName,
                              String patientDni,
                              String doctorFullName,
                              PaymentMethod paymentMethod,
                              String observations,
                              Long voucherId,
                              Long voucherDetailId) {
        this.id = id;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.currencyType = currencyType;
        this.exchangeRate = exchangeRate;
        this.convertedAmount = convertedAmount;
        this.patientFullName = patientFullName;
        this.patientDni = patientDni;
        this.doctorFullName = doctorFullName;
        this.paymentMethod = paymentMethod;
        this.observations = observations;
        this.voucherId = voucherId;
        this.voucherDetailId = voucherDetailId;
    }

    public Long getId() { return id; }
    public LocalDate getReceiptDate() { return receiptDate; }
    public BigDecimal getAmount() { return amount; }
    public CurrencyType getCurrencyType() { return currencyType; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public String getPatientFullName() { return patientFullName; }
    public String getPatientDni() { return patientDni; }
    public String getDoctorFullName() { return doctorFullName; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getObservations() { return observations; }
    public Long getVoucherId() { return voucherId; }
    public Long getVoucherDetailId() { return voucherDetailId; }
}