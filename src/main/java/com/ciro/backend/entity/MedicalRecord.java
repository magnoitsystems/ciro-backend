package com.ciro.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column
    private String evaluation;

    @Column
    private String file;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_shift")
    @Nullable
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private User doctor;
}