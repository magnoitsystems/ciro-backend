package com.ciro.backend.entity;

import com.ciro.backend.enums.DocumentType;
import com.ciro.backend.enums.HealthInsurance;
import com.ciro.backend.enums.PatientFrom;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String phone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(nullable = false, unique = true)
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(name = "obra_social")
    private HealthInsurance obraSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_from")
    private PatientFrom from;

    @Column
    private String observations;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}