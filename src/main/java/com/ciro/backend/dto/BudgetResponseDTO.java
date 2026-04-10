package com.ciro.backend.dto;

import com.ciro.backend.enums.BudgetStatus;

import java.time.LocalDate;

public class BudgetResponseDTO {
    private Long id;
    private LocalDate uploadedDate;
    private LocalDate date;
    private BudgetStatus status;
    private Long patientId;
    private String patientFullName;
    private String fileUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getUploadedDate() { return uploadedDate; }
    public void setUploadedDate(LocalDate uploadedDate) { this.uploadedDate = uploadedDate; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BudgetStatus getStatus() { return status; }
    public void setStatus(BudgetStatus status) { this.status = status; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientFullName() { return patientFullName; }
    public void setPatientFullName(String patientFullName) { this.patientFullName = patientFullName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}