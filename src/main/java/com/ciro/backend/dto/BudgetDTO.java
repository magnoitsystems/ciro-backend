package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetDTO {
    private LocalDate uploadedDate;
    private Patient patient;
    private String file_url;
}
