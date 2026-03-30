package com.ciro.backend.controller;

import com.ciro.backend.dto.MedicalRecordDTO;
import com.ciro.backend.entity.MedicalRecord;
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
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id) {
        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getMedicalRecordById(id);

        return new ResponseEntity<>(medicalRecordDTO, HttpStatus.OK);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordByDoctorId(@PathVariable Long id) {
        List<MedicalRecordDTO> medicalRecordDTO = medicalRecordService.getMedicalRecordsByDoctor(id);

        return new ResponseEntity<>(medicalRecordDTO, HttpStatus.OK);
    }

    @GetMapping("/patient/{dni}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByDniPatient(@PathVariable String dni) {
        List<MedicalRecordDTO> medicalRecordDTO = medicalRecordService.getMedicalRecordByDNIPatient(dni);

        return new ResponseEntity<>(medicalRecordDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord medicalRecord = medicalRecordService.createMedicalRecord(medicalRecordDTO);
        return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO, @PathVariable Long id) {
        MedicalRecordDTO medicalRecordUpdate = medicalRecordService.updateMedicalRecord(medicalRecordDTO, id);

        return new ResponseEntity<>(medicalRecordUpdate, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MedicalRecordDTO> deleteMedicalRecord(@PathVariable Long id) {
        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getMedicalRecordById(id);
        medicalRecordService.deleteMedicalRecord(id);

        return new ResponseEntity<>(medicalRecordDTO, HttpStatus.OK);
    }
}
