package com.ciro.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "suppliers")
public class Supplier {
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
    private String observations;
}