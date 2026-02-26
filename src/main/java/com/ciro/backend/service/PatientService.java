package com.ciro.backend.service;

import com.ciro.backend.dto.PatientDTO;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.User;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Patient createPatient(PatientDTO dto) {
        if (patientRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("El paciente con DNI " + dto.getDni() + " ya existe.");
        }

        User creator = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Usuario secretario no encontrado"));

        Patient newPatient = new Patient();
        newPatient.setFullName(dto.getFullName());
        newPatient.setAddress(dto.getAddress());
        newPatient.setCity(dto.getCity());
        newPatient.setPhone(dto.getPhone());
        newPatient.setBirthDate(dto.getBirthDate());
        newPatient.setDocumentType(dto.getDocumentType());
        newPatient.setDni(dto.getDni());
        newPatient.setObraSocial(dto.getObraSocial());
        newPatient.setFrom(dto.getFrom());
        newPatient.setObservations(dto.getObservations());
        newPatient.setCreatedBy(creator);

        return patientRepository.save(newPatient);
    }

    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PatientDTO mapToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setFullName(patient.getFullName());
        dto.setAddress(patient.getAddress());
        dto.setCity(patient.getCity());
        dto.setPhone(patient.getPhone());
        dto.setBirthDate(patient.getBirthDate());
        dto.setDocumentType(patient.getDocumentType());
        dto.setDni(patient.getDni());
        dto.setObraSocial(patient.getObraSocial());
        dto.setFrom(patient.getFrom());
        dto.setObservations(patient.getObservations());

        if (patient.getCreatedBy() != null) {
            dto.setCreatedById(patient.getCreatedBy().getId());
        }

        return dto;
    }

    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return mapToDTO(patient);
    }

    public List<PatientDTO> searchPatients(String dni, String fullName, String city) {
        List<Patient> patients = patientRepository.findByFilters(dni, fullName, city);

        return patients.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}