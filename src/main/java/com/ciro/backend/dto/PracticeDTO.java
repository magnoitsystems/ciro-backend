package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.ImplantType;
import com.ciro.backend.enums.SurgeryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PracticeDTO {
    private Patient patient;
    private User doctor;
    private LocalDate practiceDate;
    private String practiceType;
    private Boolean reimplantation;
    private BigDecimal amount;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public LocalDate getPracticeDate() {
        return practiceDate;
    }

    public void setPracticeDate(LocalDate practiceDate) {
        this.practiceDate = practiceDate;
    }


    public Boolean getReimplantation() {
        return reimplantation;
    }

    public void setReimplantation(Boolean reimplantation) {
        this.reimplantation = reimplantation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }
}
