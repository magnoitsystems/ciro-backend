package com.ciro.backend.service;

import com.ciro.backend.dto.MedicalRecordDTO;
import com.ciro.backend.entity.MedicalRecord;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public MedicalRecordDTO updateMedicalRecord(MedicalRecordDTO medicalRecord, Long id) {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte médico con ID: " + id));

        if(medicalRecord.getRecordDate() != null){
            existingMedicalRecord.setRecordDate(medicalRecord.getRecordDate());
        }
        if(medicalRecord.getFile() != null){
            existingMedicalRecord.setFile(medicalRecord.getFile());
        }
        if(medicalRecord.getEvaluation() != null){
            existingMedicalRecord.setEvaluation(medicalRecord.getEvaluation());
        }
        if(medicalRecord.getDoctor() != null){
            existingMedicalRecord.setDoctor(medicalRecord.getDoctor());
        }
        if(medicalRecord.getShift() != null){
            existingMedicalRecord.setShift(medicalRecord.getShift());
        }

        MedicalRecord updatedMedicalReport = medicalRecordRepository.save(existingMedicalRecord);

        return mapToDTO(updatedMedicalReport);
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

    //GETS

    //All medical records
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        List<MedicalRecordDTO> dtos = new ArrayList<>();
        for (MedicalRecord medicalRecord : medicalRecords) {
            dtos.add(mapToDTO(medicalRecord));
        }
        return dtos;
    }

    //By medical record id
    public MedicalRecordDTO getMedicalRecordById(Long id) {
        if(id >= 0){
            MedicalRecord medicalRecord = medicalRecordRepository.findById(id).isPresent() ? medicalRecordRepository.findById(id).get() : null;
            if(medicalRecord != null){
                return mapToDTO(medicalRecord);
            }
            return null;
        }
        return null;
    }

    //By doctor id
    public List<MedicalRecordDTO> getMedicalRecordsByDoctor(Long doctorId) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordByDoctor(doctorId);

        List<MedicalRecordDTO> dtos = new ArrayList<>();

        for (MedicalRecord medicalRecord : medicalRecords) {
            dtos.add(mapToDTO(medicalRecord));
        }

        return dtos;
    }

    //By patient DNI
    public List<MedicalRecordDTO> getMedicalRecordByDNIPatient(String dni) {
        List<MedicalRecord> medicalRecord =  medicalRecordRepository.getMedicalRecordByPatient(dni);
        List<MedicalRecordDTO> dtos = new ArrayList<>();

        if(medicalRecord == null){
            throw new RuntimeException("El paciente con DNI " + dni + " no existe.");
        }

        for (MedicalRecord medicalRecord1 : medicalRecord) {
            dtos.add(mapToDTO(medicalRecord1));
        }

        return dtos;
    }
}
