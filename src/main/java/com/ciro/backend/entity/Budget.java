package com.ciro.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "budget_date")
    private LocalDate uploadedDate;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Column
    private String file_url;
}
