package com.ciro.backend.controller;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.LabelPatient;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody PatientCreateDTO patientDTO) {
        PatientResponseDTO newPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(newPatient, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponseDTO>> searchPatients(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String city
    ) {
        return ResponseEntity.ok(patientService.searchPatients(dni, fullName, city));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientUpdateDTO updateDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


    @GetMapping("/statistics/{label}")
    public ResponseEntity<StatisticsDTO> getStatistics(@PathVariable Long label) {
        StatisticsDTO statisticsDTO = patientService.getPatientsAndStatistics(label);

        return ResponseEntity.ok(statisticsDTO);
    }

    @GetMapping("/debtors")
    public ResponseEntity<List<PatientDebtorDTO>> getDebtorPatients() {
        List<PatientDebtorDTO> debtors = patientService.getDebtorPatients();
        return ResponseEntity.ok(debtors);
    }
}