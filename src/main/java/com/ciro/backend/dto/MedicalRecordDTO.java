package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.User;
import lombok.Data;

import java.time.LocalDate;

public class MedicalRecordDTO {
    private LocalDate recordDate;
    private String evaluation;
    private String file;
    private Patient patient;
    private Shift shift;
    private User doctor;

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
}
