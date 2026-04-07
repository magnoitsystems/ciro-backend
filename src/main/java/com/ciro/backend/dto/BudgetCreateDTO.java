package com.ciro.backend.dto;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class BudgetCreateDTO {
    private LocalDate uploadedDate;
    private Long patientId;
    private MultipartFile file;

    public LocalDate getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDate uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}