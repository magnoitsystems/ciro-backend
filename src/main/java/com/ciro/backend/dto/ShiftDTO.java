package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.ShiftStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShiftDTO {
    private Patient patient;
    private User doctor;
    private LocalDateTime shiftDate;
    private ShiftStatus status;
}
