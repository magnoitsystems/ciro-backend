package com.ciro.backend.dto;

import java.util.List;
public class PatientCohortStatDTO {
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
