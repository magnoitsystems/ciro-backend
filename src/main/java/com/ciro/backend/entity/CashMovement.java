package com.ciro.backend.entity;

import com.ciro.backend.enums.CashMovementType;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "cash_movements")
public class CashMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type")
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "movement_date")
    private LocalDateTime movementDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type")
    private CashMovementType type;

    @Column
    private String observations;

    @Column(name = "doctor_id")
    private Long doctorId;

    public Long getId() {
        return id;
    }

    public Long getDoctorId(){
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
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
}