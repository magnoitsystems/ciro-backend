package com.ciro.backend.dto;

import java.math.BigDecimal;

public class PatientDebtorDTO {
    private Long id;
    private String dni;
    private String fullName;
    private BigDecimal debtPesos;
    private BigDecimal debtDolares;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getDebtPesos() {
        return debtPesos;
    }

    public void setDebtPesos(BigDecimal debtPesos) {
        this.debtPesos = debtPesos;
    }

    public BigDecimal getDebtDolares() {
        return debtDolares;
    }

    public void setDebtDolares(BigDecimal debtDolares) {
        this.debtDolares = debtDolares;
    }
}