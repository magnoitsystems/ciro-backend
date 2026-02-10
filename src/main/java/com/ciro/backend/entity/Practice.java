package com.ciro.backend.entity;

import com.ciro.backend.enums.SurgeryType;
import com.ciro.backend.enums.ImplantType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Data
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
}