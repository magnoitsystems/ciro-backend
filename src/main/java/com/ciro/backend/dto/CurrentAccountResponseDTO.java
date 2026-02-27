package com.ciro.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class CurrentAccountResponseDTO {
    private Long patientId;
    private String patientFullName;
    private BigDecimal debtInPesos;
    private BigDecimal debtInDollars;
    private List<CurrentAccountMovementDTO> movements;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getPatientFullName() { return patientFullName; }
    public void setPatientFullName(String patientFullName) { this.patientFullName = patientFullName; }
    public BigDecimal getDebtInPesos() { return debtInPesos; }
    public void setDebtInPesos(BigDecimal debtInPesos) { this.debtInPesos = debtInPesos; }
    public BigDecimal getDebtInDollars() { return debtInDollars; }
    public void setDebtInDollars(BigDecimal debtInDollars) { this.debtInDollars = debtInDollars; }
    public List<CurrentAccountMovementDTO> getMovements() { return movements; }
    public void setMovements(List<CurrentAccountMovementDTO> movements) { this.movements = movements; }
}