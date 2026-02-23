package com.ciro.backend.controller;

import com.ciro.backend.dto.PatientDTO;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.service.PatientService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDTO patientDTO) {
        Patient newPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(newPatient, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> searchPatients(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String city
    ) {
        List<PatientDTO> results = patientService.searchPatients(dni, fullName, city);
        return ResponseEntity.ok(results);
    }
}