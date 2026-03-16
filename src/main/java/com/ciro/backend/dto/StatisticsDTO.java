package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import lombok.Data;

import java.util.List;
public class StatisticsDTO {
    private int count;
    private List<PatientResponseDTO> patients;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PatientResponseDTO> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientResponseDTO> patients) {
        this.patients = patients;
    }
}
