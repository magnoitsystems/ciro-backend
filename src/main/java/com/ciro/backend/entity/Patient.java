package com.ciro.backend.entity;

import com.ciro.backend.enums.PatientFrom;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String dni;

    @Column
    private String obraSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_from")
    private PatientFrom from;

    @Column
    private String observations;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User created_by;
}