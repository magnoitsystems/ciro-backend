package com.ciro.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class StatItemDTO {
    private String label;
    private BigDecimal amount;
    private Long count;
    private Double percentage;
    private List<Long> referenceIds;

    public StatItemDTO(String label, Long count, Double percentage, List<Long> referenceIds) {
        this.label = label;
        this.count = count;
        this.percentage = percentage;
        this.referenceIds = referenceIds;
    }

    public StatItemDTO(String label, BigDecimal amount, Double percentage, List<Long> referenceIds) {
        this.label = label;
        this.amount = amount;
        this.percentage = percentage;
        this.referenceIds = referenceIds;
    }
}