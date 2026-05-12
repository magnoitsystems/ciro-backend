package com.ciro.backend.dto;

import com.ciro.backend.enums.ShiftStatus;
import java.time.LocalDateTime;

public class ShiftCreateDTO {
    private String patientDni;
    private Long doctorId;
    private LocalDateTime shiftDate;
    private LocalDateTime endDate;
    private ShiftStatus status;
    private String noteDescription;

    public String getPatientDni() { return patientDni; }
    public void setPatientDni(String patientDni) { this.patientDni = patientDni; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getShiftDate() { return shiftDate; }
    public void setShiftDate(LocalDateTime shiftDate) { this.shiftDate = shiftDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public ShiftStatus getStatus() { return status; }
    public void setStatus(ShiftStatus status) { this.status = status; }

    public String getNoteDescription() { return noteDescription; }
    public void setNoteDescription(String noteDescription) { this.noteDescription = noteDescription; }
}