package com.ciro.backend.entity;

import com.ciro.backend.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voucher_date")
    private LocalDate voucherDate;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Column
    private String observations;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type", nullable = false)
    private CurrencyType currencyType;

    @Column
    private BigDecimal total_amount;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoucherDetail> details;

    public List<VoucherDetail> getDetails() {
        return details;
    }

    public void setDetails(List<VoucherDetail> details) {
        this.details = details;
    }

    public CurrencyType getCurrencyType() { return currencyType; }
    public void setCurrencyType(CurrencyType currencyType) { this.currencyType = currencyType; }

    public Long getId() {
        return id;
    }

    public LocalDate getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(LocalDate voucherDate) {
        this.voucherDate = voucherDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }
}