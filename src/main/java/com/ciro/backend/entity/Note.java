package com.ciro.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "id_shift", nullable = true)
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "id_task", nullable = true)
    private Task task;
}