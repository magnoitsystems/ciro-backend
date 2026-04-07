package com.ciro.backend.dto;

import com.ciro.backend.enums.ShiftStatus;
import java.time.LocalDateTime;

public class ShiftResponseDTO {
    private Long id;
    private LocalDateTime shiftDate;
    private ShiftStatus status;
    private String patientDni;
    private String patientFullName;
    private Long doctorId;
    private String doctorFullName;
    private String noteDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDateTime shiftDate) {
        this.shiftDate = shiftDate;
    }

    public ShiftStatus getStatus() {
        return status;
    }

    public void setStatus(ShiftStatus status) {
        this.status = status;
    }

    public String getPatientDni() {
        return patientDni;
    }

    public void setPatientDni(String patientDni) {
        this.patientDni = patientDni;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorFullName() {
        return doctorFullName;
    }

    public void setDoctorFullName(String doctorFullName) {
        this.doctorFullName = doctorFullName;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }
}