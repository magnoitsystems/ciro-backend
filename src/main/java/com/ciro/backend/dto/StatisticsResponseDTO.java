package com.ciro.backend.dto;

public class StatisticsResponseDTO {
    private FinancialStatsDTO financial;
    private PatientStatsDTO patients;

    public FinancialStatsDTO getFinancial() {
        return financial;
    }

    public void setFinancial(FinancialStatsDTO financial) {
        this.financial = financial;
    }

    public PatientStatsDTO getPatients() {
        return patients;
    }

    public void setPatients(PatientStatsDTO patients) {
        this.patients = patients;
    }
}

