package com.ciro.backend.entity;

import com.ciro.backend.enums.SurgeryType;
import com.ciro.backend.enums.ImplantType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "practices")
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private User doctor;

    @Column(name = "practice_date")
    private LocalDate practiceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "surgery_type")
    private SurgeryType surgeryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "implant_type")
    private ImplantType implantType;

    private Boolean reimplantation;

    private BigDecimal amount;

    public Long getId() {
        return id;
    }

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