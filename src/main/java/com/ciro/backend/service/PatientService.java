package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.TaskPriority;
import com.ciro.backend.enums.TaskStatus;
import com.ciro.backend.exception.DuplicateResourceException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.LabelPatientRepository;
import com.ciro.backend.repository.LabelRepository;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelPatientRepository labelPatientRepository;
    @Autowired
    private PracticeService practiceService;
    @Autowired
    private TaskService taskService;

    @Transactional
    public Patient createPatient(PatientDTO dto) {
        if (patientRepository.existsByDni(dto.getDni())) {
            throw new DuplicateResourceException("El paciente con el DNI " + dto.getDni() + " ya existe en el sistema");
        }

        User creator = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + dto.getCreatedById() + " no existe"));

        Patient newPatient = new Patient();
        newPatient.setFullName(dto.getFullName());
        newPatient.setAddress(dto.getAddress());
        newPatient.setCity(dto.getCity());
        newPatient.setPhone(dto.getPhone());
        newPatient.setBirthDate(dto.getBirthDate());
        newPatient.setDocumentType(dto.getDocumentType());
        newPatient.setDni(dto.getDni());
        newPatient.setObraSocial(dto.getObraSocial());

        if (dto.getFrom() != null) {
            newPatient.setFrom(dto.getFrom());
        } else {
            TaskDTO automaticTaskDTO = new TaskDTO();
            automaticTaskDTO.setUser(creator);
            automaticTaskDTO.setDescription(newPatient.getDni());
            automaticTaskDTO.setStatus(TaskStatus.PENDING);
            automaticTaskDTO.setPriority(TaskPriority.LOW);
            automaticTaskDTO.setTitle("Buscar información de cómo nos conocieron");
            createTask(automaticTaskDTO);
        }
        newPatient.setObservations(dto.getObservations());
        newPatient.setCreatedBy(creator);

        return patientRepository.save(newPatient);
    }

    private void createTask(TaskDTO automaticTaskDTO) {
        taskService.save(automaticTaskDTO, null);
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

    public void deletePatient(Long id){
        if(id >= 0){
            patientRepository.deleteById(id);
        }
    }

    @Transactional
    public LabelPatient assignLabel(Long patientId, Long labelId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label no encontrado"));

        // evitar duplicados
        LabelPatient alreadyExists = labelPatientRepository.existsByPatientIdAndLabelId(patientId, labelId);

        if (alreadyExists != null) {
            throw new RuntimeException("El paciente ya tiene este label");
        }

        LabelPatient patientLabel = new LabelPatient();
        patientLabel.setPatient(patient);
        patientLabel.setLabel(label);

        return labelPatientRepository.save(patientLabel);
    }

    public StatisticsDTO getPatientsAndStatistics(Long labelId) {

        labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label no encontrado"));

        List<LabelPatient> relations =
                labelPatientRepository.findLabelPatientByLabel(labelId);

        List<Patient> patients = relations.stream()
                .map(LabelPatient::getPatient)
                .toList();

        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setCount(patients.size());
        statisticsDTO.setPatients(patients);

        return statisticsDTO;
    }
}