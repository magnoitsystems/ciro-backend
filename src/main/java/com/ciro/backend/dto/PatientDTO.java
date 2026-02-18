package com.ciro.backend.dto;

import com.ciro.backend.enums.DocumentType;
import com.ciro.backend.enums.HealthInsurance;
import com.ciro.backend.enums.PatientFrom;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDTO {
    private String fullName;
    private String address;
    private String city;
    private String phone;
    private LocalDate birthDate;
    private DocumentType documentType;
    private String dni;
    private HealthInsurance obraSocial;
    private PatientFrom from;
    private String observations;
    private Long doctorId;
    private Long createdById;
}