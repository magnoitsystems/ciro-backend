// Archivo: src/main/java/com/ciro/backend/dto/FinancialStatsDTO.java
package com.ciro.backend.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinancialStatsDTO {
    private BigDecimal currentMonthIncomePesos;
    private BigDecimal currentMonthIncomeDollars;
    private BigDecimal currentMonthExpensesPesos;
    private BigDecimal currentMonthExpensesDollars;

    private BigDecimal previousMonthIncomePesos;
    private BigDecimal previousMonthIncomeDollars;

    private Double percentageTransfer;
    private Double percentageCash;
    private Double percentageCard;
}
