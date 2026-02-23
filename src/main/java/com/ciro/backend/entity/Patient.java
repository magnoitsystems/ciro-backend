package com.ciro.backend.entity;

import com.ciro.backend.enums.DocumentType;
import com.ciro.backend.enums.HealthInsurance;
import com.ciro.backend.enums.PatientFrom;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate; // Importante para fechas

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JoinColumn(name = "id_doctor")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public HealthInsurance getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(HealthInsurance obraSocial) {
        this.obraSocial = obraSocial;
    }

    public PatientFrom getFrom() {
        return from;
    }

    public void setFrom(PatientFrom from) {
        this.from = from;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}