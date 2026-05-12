package com.ciro.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrillDownDetailDTO {
    private Long id;
    private String primaryText;
    private String secondaryText;
}