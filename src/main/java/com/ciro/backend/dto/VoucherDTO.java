package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VoucherDTO {
    private Long id;
    private String patientFullName;
    private String professionalFullName;
    private LocalDate voucherDate;
    private CurrencyType currency;
    private String observations;
    private BigDecimal totalAmount;
    private List<VoucherDetailDTO> details;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientFullName() { return patientFullName; }
    public void setPatientFullName(String patientFullName) { this.patientFullName = patientFullName; }

    public String getProfessionalFullName() { return professionalFullName; }
    public void setProfessionalFullName(String professionalFullName) { this.professionalFullName = professionalFullName; }

    public LocalDate getVoucherDate() { return voucherDate; }
    public void setVoucherDate(LocalDate voucherDate) { this.voucherDate = voucherDate; }

    public CurrencyType getCurrency() { return currency; }
    public void setCurrency(CurrencyType currency) { this.currency = currency; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<VoucherDetailDTO> getDetails() { return details; }
    public void setDetails(List<VoucherDetailDTO> details) { this.details = details; }
}