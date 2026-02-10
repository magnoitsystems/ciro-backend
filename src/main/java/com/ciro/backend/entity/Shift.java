package com.ciro.backend.entity;

import com.ciro.backend.enums.ShiftStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private User doctor;

    @Column(name = "shift_date")
    private LocalDateTime shiftDate;

    @Enumerated(EnumType.STRING)
    private ShiftStatus status;
}