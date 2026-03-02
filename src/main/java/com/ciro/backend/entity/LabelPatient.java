package com.ciro.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "label_patients")
public class LabelPatient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_label")
    private Label label;

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}