package com.ciro.backend.service;

import com.ciro.backend.dto.PatientDTO;
import com.ciro.backend.dto.PatientUpdateDTO;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.User;
import com.ciro.backend.exception.DuplicateResourceException;
import com.ciro.backend.exception.ResourceNotFoundException;
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
            throw new DuplicateResourceException("El paciente con el DNI "+ dto.getDni()+" ya existe en el sistema");
        }

        User creator = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID "+ dto.getCreatedById() +" no existe"));

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
                .orElseThrow(() -> new ResourceNotFoundException("El paciente con ID "+ id  +" no existe"));
        return mapToDTO(patient);
    }

    public List<PatientDTO> searchPatients(String dni, String fullName, String city) {
        // si no se enviaron todos los parámetros enviamos vacío para que no rompa
        String safeDni = (dni != null) ? dni : "";
        String safeFullName = (fullName != null) ? fullName : "";
        String safeCity = (city != null) ? city : "";

        List<Patient> patients = patientRepository.findByFilters(safeDni, safeFullName, safeCity);

        return patients.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientUpdateDTO updateDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el paciente con ID: " + id));

        existingPatient.setFullName(updateDTO.getFullName());
        existingPatient.setAddress(updateDTO.getAddress());
        existingPatient.setCity(updateDTO.getCity());
        existingPatient.setPhone(updateDTO.getPhone());
        existingPatient.setBirthDate(updateDTO.getBirthDate());
        existingPatient.setObraSocial(updateDTO.getObraSocial());
        existingPatient.setFrom(updateDTO.getFrom());
        existingPatient.setObservations(updateDTO.getObservations());

        Patient updatedPatient = patientRepository.save(existingPatient);

        return mapToDTO(updatedPatient);
    }
}