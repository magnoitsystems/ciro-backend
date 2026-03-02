package com.ciro.backend.dto;

import com.ciro.backend.entity.Patient;
import lombok.Data;

import java.util.List;

public class StatisticsDTO {
    private int count;
    private List<Patient> patients;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
