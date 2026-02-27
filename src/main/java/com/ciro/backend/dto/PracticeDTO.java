package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.ImplantType;
import com.ciro.backend.enums.SurgeryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PracticeDTO {
    private Patient patient;
    private User doctor;
    private LocalDate practiceDate;
    private SurgeryType surgeryType;
    private ImplantType implantType;
    private Boolean reimplantation;
    private BigDecimal amount;
}
