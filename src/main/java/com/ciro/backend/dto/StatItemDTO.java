package com.ciro.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatItemDTO {
    private String label;
    private BigDecimal amount;
    private Long count;
    private Double percentage;

    public StatItemDTO(String label, Long count, Double percentage) {
        this.label = label;
        this.count = count;
        this.percentage = percentage;
    }

    public StatItemDTO(String label, BigDecimal amount, Double percentage) {
        this.label = label;
        this.amount = amount;
        this.percentage = percentage;
    }
}