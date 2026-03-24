package com.ciro.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class StatisticsResponseDTO {
    private FinancialStatsDTO financial;
    private PatientStatsDTO patients;
    private Long implantsThisMonth;
}

