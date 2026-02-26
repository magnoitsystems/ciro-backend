package com.ciro.backend.entity;

import com.ciro.backend.enums.ShiftStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private User doctor;

    @Column(name = "shift_date")
    private LocalDateTime shiftDate;

    @Enumerated(EnumType.STRING)
    private ShiftStatus status;

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

    public LocalDateTime getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDateTime shiftDate) {
        this.shiftDate = shiftDate;
    }

    public ShiftStatus getStatus() {
        return status;
    }

    public void setStatus(ShiftStatus status) {
        this.status = status;
    }
}