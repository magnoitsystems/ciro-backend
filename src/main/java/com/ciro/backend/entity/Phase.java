package com.ciro.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "phases")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phase_name")
    private String phaseName;

    @ManyToOne
    @JoinColumn(name = "id_budget")
    private Budget budget;
}