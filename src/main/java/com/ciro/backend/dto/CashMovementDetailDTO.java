package com.ciro.backend.dto;

import com.ciro.backend.enums.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CashMovementDetailDTO {
    private Long id;
    private BigDecimal amount;
    private CurrencyType currencyType;
    private PaymentMethod paymentMethod;
    private LocalDateTime movementDate;
    private CashMovementType type;
    private String observations;
    private Long doctorId;

    private Object relatedEntity;

    private List<PercentageSplitDTO> suggestedSplits;

    public static class PercentageSplitDTO {
        private String label;
        private BigDecimal doctorAmount;
        private BigDecimal clinicAmount;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public BigDecimal getDoctorAmount() {
            return doctorAmount;
        }

        public void setDoctorAmount(BigDecimal doctorAmount) {
            this.doctorAmount = doctorAmount;
        }

        public BigDecimal getClinicAmount() {
            return clinicAmount;
        }

        public void setClinicAmount(BigDecimal clinicAmount) {
            this.clinicAmount = clinicAmount;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    public CashMovementType getType() {
        return type;
    }

    public void setType(CashMovementType type) {
        this.type = type;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Object getRelatedEntity() {
        return relatedEntity;
    }

    public void setRelatedEntity(Object relatedEntity) {
        this.relatedEntity = relatedEntity;
    }

    public List<PercentageSplitDTO> getSuggestedSplits() {
        return suggestedSplits;
    }

    public void setSuggestedSplits(List<PercentageSplitDTO> suggestedSplits) {
        this.suggestedSplits = suggestedSplits;
    }
}