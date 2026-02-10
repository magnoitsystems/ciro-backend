package com.ciro.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "voucher_details")
public class VoucherDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String detail;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column
    private int amount;

    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;
}