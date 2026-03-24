package com.ciro.backend.dto;
import lombok.Data;
import java.util.Map;

@Data
public class PatientStatsDTO {
    private long totalDebtors;
    private long totalNonDebtors;

    private Map<String, Long> patientsByOrigin;
    private Map<String, Long> patientsByCity;
}