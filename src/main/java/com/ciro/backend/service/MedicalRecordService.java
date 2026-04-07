package com.ciro.backend.service;

import com.ciro.backend.dto.MedicalRecordCreateDTO;
import com.ciro.backend.dto.MedicalRecordResponseDTO;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Transactional
    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordCreateDTO dto) {

        Patient patient = patientRepository.findByDni(dto.getPatientDni());
        if (patient == null) {
            throw new ResourceNotFoundException("Paciente no encontrado con DNI: " + dto.getPatientDni());
        }

        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no existe"));

        MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setPatient(patient);
        newMedicalRecord.setDoctor(doctor);
        newMedicalRecord.setEvaluation(dto.getEvaluation());
        newMedicalRecord.setRecordDate(dto.getRecordDate() != null ? dto.getRecordDate() : LocalDate.now());

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                String fileUrl = cloudinaryService.uploadFile(dto.getFile());
                newMedicalRecord.setFile(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
            }
        }

        MedicalRecord savedRecord = medicalRecordRepository.save(newMedicalRecord);
        return mapToDTO(savedRecord);
    }

    @Transactional
    public MedicalRecordResponseDTO updateMedicalRecord(MedicalRecordCreateDTO dto, Long id) {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte médico con ID: " + id));

        if (dto.getRecordDate() != null) {
            existingMedicalRecord.setRecordDate(dto.getRecordDate());
        }
        if (dto.getEvaluation() != null) {
            existingMedicalRecord.setEvaluation(dto.getEvaluation());
        }

        if (dto.getDoctorId() != null) {
            User doctor = userRepository.findById(dto.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor no existe"));
            existingMedicalRecord.setDoctor(doctor);
        }

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                String fileUrl = cloudinaryService.uploadFile(dto.getFile());
                existingMedicalRecord.setFile(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
            }
        }

        MedicalRecord updatedMedicalReport = medicalRecordRepository.save(existingMedicalRecord);
        return mapToDTO(updatedMedicalReport);
    }

    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MedicalRecord no encontrado"));
        medicalRecordRepository.deleteById(id);
    }

    public MedicalRecordResponseDTO getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord no encontrado"));
        return mapToDTO(medicalRecord);
    }

    public List<MedicalRecordResponseDTO> getMedicalRecordsByDoctor(Long doctorId) {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor " + doctorId + " no encontrado"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordByDoctor(doctor.getId());
        return medicalRecords.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MedicalRecordResponseDTO> getMedicalRecordByDNIPatient(String dni) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecordByPatient(dni);
        if (medicalRecords == null || medicalRecords.isEmpty()) {
            throw new ResourceNotFoundException("El paciente con DNI " + dni + " no existe o no tiene registros.");
        }
        return medicalRecords.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public MedicalRecordResponseDTO mapToDTO(MedicalRecord medicalRecord) {
        MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
        dto.setId(medicalRecord.getId());
        dto.setEvaluation(medicalRecord.getEvaluation());
        dto.setRecordDate(medicalRecord.getRecordDate());
        dto.setFileUrl(medicalRecord.getFile()); // Acá va la URL de Cloudinary

        if (medicalRecord.getDoctor() != null) {
            dto.setDoctorId(medicalRecord.getDoctor().getId());
            dto.setDoctorFullName(medicalRecord.getDoctor().getName() + " " + medicalRecord.getDoctor().getLastname());
        }
        if (medicalRecord.getPatient() != null) {
            dto.setPatientDni(medicalRecord.getPatient().getDni());
            dto.setPatientFullName(medicalRecord.getPatient().getFullName());
        }

        return dto;
    }
}