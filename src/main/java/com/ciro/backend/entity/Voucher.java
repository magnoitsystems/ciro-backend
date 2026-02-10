package com.ciro.backend.entity;

import com.ciro.backend.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voucher_date")
    private LocalDate voucherDate;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Column
    private String observations;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}