package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import lombok.Data;

import java.util.List;

@Data
public class StatisticsDTO {
    private int count;
    private List<Patient> patients;
}
