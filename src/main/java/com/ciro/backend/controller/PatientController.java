package com.ciro.backend.controller;

import com.ciro.backend.dto.PatientDTO;
import com.ciro.backend.dto.PatientUpdateDTO;
import com.ciro.backend.dto.PracticeDTO;
import com.ciro.backend.dto.StatisticsDTO;
import com.ciro.backend.entity.LabelPatient;
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

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientUpdateDTO updateDTO) {

        PatientDTO updatedPatient = patientService.updatePatient(id, updateDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<PatientDTO> deletePatient(@PathVariable Long id) {
        PatientDTO patientDTO = patientService.getPatientById(id);
        if(patientDTO != null){
            patientService.deletePatient(id);
            return ResponseEntity.ok(patientDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{patientId}/labels/{labelId}")
    public ResponseEntity<LabelPatient> assignLabel(
            @PathVariable Long patientId,
            @PathVariable Long labelId) {

        LabelPatient labelPatient = patientService.assignLabel(patientId, labelId);

        if(labelPatient == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics/{label}")
    public ResponseEntity<StatisticsDTO> getStatistics(@PathVariable Long label) {
        StatisticsDTO statisticsDTO = patientService.getPatientsAndStatistics(label);

        if(statisticsDTO == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(statisticsDTO);
    }
}