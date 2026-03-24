package com.ciro.backend.dto;

import java.util.List;
public class PatientStatsDTO {
    private long totalPatients;
    private long totalDebtors;
    private long totalNonDebtors;

    private List<StatItemDTO> patientsByOrigin;
    private List<StatItemDTO> patientsByCity;

    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public long getTotalDebtors() {
        return totalDebtors;
    }

    public void setTotalDebtors(long totalDebtors) {
        this.totalDebtors = totalDebtors;
    }

    public long getTotalNonDebtors() {
        return totalNonDebtors;
    }

    public void setTotalNonDebtors(long totalNonDebtors) {
        this.totalNonDebtors = totalNonDebtors;
    }

    public List<StatItemDTO> getPatientsByOrigin() {
        return patientsByOrigin;
    }

    public void setPatientsByOrigin(List<StatItemDTO> patientsByOrigin) {
        this.patientsByOrigin = patientsByOrigin;
    }

    public List<StatItemDTO> getPatientsByCity() {
        return patientsByCity;
    }

    public void setPatientsByCity(List<StatItemDTO> patientsByCity) {
        this.patientsByCity = patientsByCity;
    }
}