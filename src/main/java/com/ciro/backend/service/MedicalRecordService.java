package com.ciro.backend.service;

import com.ciro.backend.dto.MedicalRecordDTO;
import com.ciro.backend.entity.MedicalRecord;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.User;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.MedicalRecordRepository;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.ShiftRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShiftRepository shiftRepository;


    @Transactional
    public MedicalRecord createMedicalRecord(MedicalRecordDTO dto) {

        Patient patient = patientRepository
                .findByDni(dto.getPatient().getDni());
//                .orElseThrow(() -> new RuntimeException("Paciente no existe"));

        User doctor = userRepository
                .findById(dto.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor no existe"));

        Shift shift = new Shift();
        if(dto.getShift() != null) {
            shift = shiftRepository
                    .findById(dto.getShift().getId())
                    .orElse(null); // si puede ser null
        } else {
            shift = null;
        }

        MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setPatient(patient); // entidad gestionada
        newMedicalRecord.setDoctor(doctor);
        newMedicalRecord.setShift(shift);
        newMedicalRecord.setRecordDate(dto.getRecordDate());
        newMedicalRecord.setFile(dto.getFile());
        newMedicalRecord.setEvaluation(dto.getEvaluation());

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

    public void deleteMedicalRecord(Long id) {
        if(id >= 0){
            medicalRecordRepository.deleteById(id);
        }
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

        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        MedicalRecord medicalRecord = medicalRecordRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("MedicalRecord no encontrado"));

        return mapToDTO(medicalRecord);
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
