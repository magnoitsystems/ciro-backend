package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import java.time.LocalDate;
import java.util.List;

public class VoucherCreateDTO {
    private Long patientId;
    private Long userId;
    private LocalDate voucherDate;
    private CurrencyType currency;
    private String observations;
    private List<VoucherDetailDTO> details;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getVoucherDate() { return voucherDate; }
    public void setVoucherDate(LocalDate voucherDate) { this.voucherDate = voucherDate; }
    public CurrencyType getCurrency() { return currency; }
    public void setCurrency(CurrencyType currency) { this.currency = currency; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public List<VoucherDetailDTO> getDetails() { return details; }
    public void setDetails(List<VoucherDetailDTO> details) { this.details = details; }
}