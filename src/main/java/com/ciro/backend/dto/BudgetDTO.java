package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import lombok.Data;

import java.time.LocalDate;

public class BudgetDTO {
    private LocalDate uploadedDate;
    private Patient patient;
    private String file_url;

    public LocalDate getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDate uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}
