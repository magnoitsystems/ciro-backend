package com.ciro.backend.controller;

import com.ciro.backend.dto.MedicalRecordCreateDTO;
import com.ciro.backend.dto.MedicalRecordResponseDTO;
import com.ciro.backend.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicalRecords")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(@PathVariable Long id) {
        return new ResponseEntity<>(medicalRecordService.getMedicalRecordById(id), HttpStatus.OK);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordByDoctorId(@PathVariable Long id) {
        return new ResponseEntity<>(medicalRecordService.getMedicalRecordsByDoctor(id), HttpStatus.OK);
    }

    @GetMapping("/patient/{dni}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByDniPatient(@PathVariable String dni) {
        return new ResponseEntity<>(medicalRecordService.getMedicalRecordByDNIPatient(dni), HttpStatus.OK);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<MedicalRecordResponseDTO> createMedicalRecord(@ModelAttribute MedicalRecordCreateDTO dto) {
        MedicalRecordResponseDTO response = medicalRecordService.createMedicalRecord(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(@ModelAttribute MedicalRecordCreateDTO dto, @PathVariable Long id) {
        MedicalRecordResponseDTO response = medicalRecordService.updateMedicalRecord(dto, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}