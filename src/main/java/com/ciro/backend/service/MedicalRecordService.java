package com.ciro.backend.service;

import com.ciro.backend.dto.MedicalRecordDTO;
import com.ciro.backend.entity.MedicalRecord;
import com.ciro.backend.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Transactional
    public MedicalRecord createMedicalRecord(MedicalRecordDTO medicalRecord) {
        if(!medicalRecordRepository.existByDNIPatient(medicalRecord.getPatient().getDni())){
            throw new RuntimeException("El paciente con DNI " + medicalRecord.getPatient().getDni() + " no existe.");
        }

        MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setPatient(medicalRecord.getPatient());
        newMedicalRecord.setRecordDate(medicalRecord.getRecordDate());
        newMedicalRecord.setFile(medicalRecord.getFile());
        newMedicalRecord.setEvaluation(medicalRecord.getEvaluation());
        newMedicalRecord.setDoctor(medicalRecord.getDoctor());
        newMedicalRecord.setShift(medicalRecord.getShift());

        return medicalRecordRepository.save(newMedicalRecord);
    }

    public MedicalRecordDTO getMedicalRecordByDNIPatient(String dni) {
        MedicalRecord medicalRecord =  medicalRecordRepository.getMedicalRecordByPatient(dni);

        if(medicalRecord == null){
            throw new RuntimeException("El paciente con DNI " + dni + " no existe.");
        }

        return mapToDTO(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.findById(medicalRecord.getId()).map(medical -> {
            if(medicalRecord.getEvaluation() != null){
                medical.setEvaluation(medicalRecord.getEvaluation());
            }
            if(medicalRecord.getDoctor() != null){
                medical.setDoctor(medicalRecord.getDoctor());
            }
            if(medicalRecord.getShift() != null){
                medical.setShift(medicalRecord.getShift());
            }
            if(medicalRecord.getRecordDate() != null){
                medical.setRecordDate(medicalRecord.getRecordDate());
            }
            if(medicalRecord.getPatient() != null){
                medical.setPatient(medicalRecord.getPatient());
            }
            return medicalRecordRepository.save(medical);
        }).orElseThrow(() -> new RuntimeException("Reporte médico no encontrado"));
    }

    public MedicalRecordDTO mapToDTO(MedicalRecord medicalRecord) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setDoctor(medicalRecord.getDoctor());
        dto.setShift(medicalRecord.getShift());
        dto.setEvaluation(medicalRecord.getEvaluation());
        dto.setRecordDate(medicalRecord.getRecordDate());
        dto.setPatient(medicalRecord.getPatient());
        dto.setFile(medicalRecord.getFile());

        return dto;
    }
}
