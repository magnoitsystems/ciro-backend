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
    private SurgeryType surgeryType;
    private ImplantType implantType;
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

    public SurgeryType getSurgeryType() {
        return surgeryType;
    }

    public void setSurgeryType(SurgeryType surgeryType) {
        this.surgeryType = surgeryType;
    }

    public ImplantType getImplantType() {
        return implantType;
    }

    public void setImplantType(ImplantType implantType) {
        this.implantType = implantType;
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
}
