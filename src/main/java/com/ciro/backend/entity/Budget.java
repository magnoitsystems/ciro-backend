package com.ciro.backend.entity;

import com.ciro.backend.enums.BudgetStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uploaded_date")
    private LocalDate uploadedDate;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BudgetStatus status;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Column
    private String file_url;

    public Long getId() { return id; }

    public LocalDate getUploadedDate() { return uploadedDate; }
    public void setUploadedDate(LocalDate uploadedDate) { this.uploadedDate = uploadedDate; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BudgetStatus getStatus() { return status; }
    public void setStatus(BudgetStatus status) { this.status = status; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getFile_url() { return file_url; }
    public void setFile_url(String file_url) { this.file_url = file_url; }
}