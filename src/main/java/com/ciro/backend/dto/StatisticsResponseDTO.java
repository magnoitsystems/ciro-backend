package com.ciro.backend.dto;

import lombok.Data;

@Data
public class StatisticsResponseDTO {
    private FinancialStatsDTO financial;
    private PatientStatsDTO patients;
    private Long implantsThisMonth;
}

