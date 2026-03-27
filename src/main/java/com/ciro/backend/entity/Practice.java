package com.ciro.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "practices")
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "practice_date")
    private LocalDate practiceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "practice_type")
    private String practiceType;

    private BigDecimal amountDollars;

    private BigDecimal tc;

    private BigDecimal amountPesos;

    public Long getId() {
        return id;
    }

    public LocalDate getPracticeDate() {
        return practiceDate;
    }

    public void setPracticeDate(LocalDate practiceDate) {
        this.practiceDate = practiceDate;
    }

    public BigDecimal getAmountDollars() {
        return amountDollars;
    }

    public void setAmountDollars(BigDecimal amountDollars) {
        this.amountDollars = amountDollars;
    }

    public BigDecimal getTc() {
        return tc;
    }

    public void setTc(BigDecimal tc) {
        this.tc = tc;
    }

    public BigDecimal getAmountPesos() {
        return amountPesos;
    }

    public void setAmountPesos(BigDecimal amountPesos) {
        this.amountPesos = amountPesos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }
}