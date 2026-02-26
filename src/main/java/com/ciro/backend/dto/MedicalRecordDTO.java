package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalRecordDTO {
    private LocalDate recordDate;
    private String evaluation;
    private String file;
    private Patient patient;
    private Shift shift;
    private User doctor;
}
