package com.ciro.backend.dto;

import com.ciro.backend.enums.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BillResponseDTO {
    private Long id;
    private String entityName;
    private LocalDate billDate;
    private BigDecimal amount;
    private String description;
    private BillStatus status;
    private PaymentMethod paymentMethod;
    private CurrencyType currencyType;
    private OriginType from;
    private BillType billType;

    public BillResponseDTO(Long id, String entityName, LocalDate billDate, BigDecimal amount, String description, BillStatus status, PaymentMethod paymentMethod, CurrencyType currencyType, OriginType from, BillType billType) {
        this.id = id;
        this.entityName = entityName;
        this.billDate = billDate;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.currencyType = currencyType;
        this.from = from;
        this.billType = billType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    public OriginType getFrom() {
        return from;
    }

    public void setFrom(OriginType from) {
        this.from = from;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }
}