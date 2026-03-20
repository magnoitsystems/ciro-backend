package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import java.time.LocalDate;
import java.util.List;

public class VoucherCreateDTO {
    private Long patientId;
    private Long userId;
    private LocalDate voucherDate;
    private String observations;
    private CurrencyType currencyType;
    private List<VoucherDetailDTO> details;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getVoucherDate() { return voucherDate; }
    public void setVoucherDate(LocalDate voucherDate) { this.voucherDate = voucherDate; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public List<VoucherDetailDTO> getDetails() { return details; }
    public void setDetails(List<VoucherDetailDTO> details) { this.details = details; }
    public CurrencyType getCurrencyType() { return currencyType; }
    public void setCurrencyType(CurrencyType currencyType) { this.currencyType = currencyType; }
}