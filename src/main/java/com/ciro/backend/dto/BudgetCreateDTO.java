package com.ciro.backend.dto;

import com.ciro.backend.enums.BudgetStatus;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class BudgetCreateDTO {
    private String title;
    private LocalDate date;
    private BudgetStatus status;
    private Long patientId;
    private MultipartFile file;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BudgetStatus getStatus() { return status; }
    public void setStatus(BudgetStatus status) { this.status = status; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}