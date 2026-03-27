package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.ImplantType;
import com.ciro.backend.enums.SurgeryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PracticeDTO {
    private LocalDate practiceDate;
    private String practiceType;
    private BigDecimal amountDollars;
    private BigDecimal amountPesos;
    private BigDecimal tc;


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

    public BigDecimal getAmountPesos() {
        return amountPesos;
    }

    public void setAmountPesos(BigDecimal amountPesos) {
        this.amountPesos = amountPesos;
    }

    public BigDecimal getTc() {
        return tc;
    }

    public void setTc(BigDecimal tc) {
        this.tc = tc;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }
}
