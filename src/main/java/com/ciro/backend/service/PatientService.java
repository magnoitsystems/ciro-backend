package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.TaskPriority;
import com.ciro.backend.enums.TaskStatus;
import com.ciro.backend.exception.DuplicateResourceException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private LabelService labelService;
    @Autowired
    private LabelPatientService labelPatientService;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelPatientRepository labelPatientRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CurrentAccountRepository currentAccountRepository;

    @Transactional
    public PatientResponseDTO createPatient(PatientCreateDTO dto) {
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
        newPatient.setObservations(dto.getObservations());
        newPatient.setCreatedBy(creator);

        if (dto.getFrom() != null) {
            newPatient.setFrom(dto.getFrom());
        }

        Patient savedPatient = patientRepository.save(newPatient);

        if (dto.getCity() != null && !dto.getCity().trim().isEmpty()) {
            Label cityLabel = labelService.getOrCreateLabel(dto.getCity());

            LabelPatient lpCity = new LabelPatient();
            lpCity.setPatient(savedPatient);
            lpCity.setLabel(cityLabel);
            labelPatientService.assignLabelToPatient(lpCity);
        }

        if (dto.getFrom() != null) {
            Label fromLabel = labelService.getOrCreateLabel(dto.getFrom().name());

            LabelPatient lpFrom = new LabelPatient();
            lpFrom.setPatient(savedPatient);
            lpFrom.setLabel(fromLabel);
            labelPatientService.assignLabelToPatient(lpFrom);

        } else {
            TaskDTO automaticTaskDTO = new TaskDTO();
            automaticTaskDTO.setUser(creator);
            automaticTaskDTO.setDescription("DNI: " + savedPatient.getDni());
            automaticTaskDTO.setStatus(TaskStatus.PENDING);
            automaticTaskDTO.setPriority(TaskPriority.LOW);
            automaticTaskDTO.setTitle("Buscar información de cómo nos conocieron");
            createTask(automaticTaskDTO);
        }

        return mapToResponseDTO(savedPatient);
    }

    private void createTask(TaskDTO automaticTaskDTO) {
        taskService.save(automaticTaskDTO, null);
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El paciente con ID "+ id  +" no existe"));
        return mapToResponseDTO(patient);
    }

    public List<PatientResponseDTO> searchPatients(String dni, String fullName, String city) {
        String safeDni = (dni != null) ? dni : "";
        String safeFullName = (fullName != null) ? fullName : "";
        String safeCity = (city != null) ? city : "";

        List<Patient> patients = patientRepository.findByFilters(safeDni, safeFullName, safeCity);

        return patients.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponseDTO updatePatient(Long id, PatientUpdateDTO updateDTO) {
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

        return mapToResponseDTO(updatedPatient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el paciente con ID: " + id);
        }
        patientRepository.deleteById(id);
    }


    public List<PatientDebtorDTO> getDebtorPatients() {
        Label deudorLabel = labelRepository.findByLabel("Deudor").orElse(null);

        if (deudorLabel == null) {
            return new ArrayList<>();
        }

        List<LabelPatient> relations = labelPatientRepository.findLabelPatientByLabel(deudorLabel.getId());

        List<PatientDebtorDTO> debtors = new ArrayList<>();

        for (LabelPatient relation : relations) {
            Patient patient = relation.getPatient();

            com.ciro.backend.entity.CurrentAccount lastRecord = currentAccountRepository
                    .findTopByPatientIdOrderByIdDesc(patient.getId())
                    .orElse(null);

            BigDecimal debtPesos = BigDecimal.ZERO;
            BigDecimal debtDolares = BigDecimal.ZERO;

            if (lastRecord != null && (lastRecord.getCanceled() == null || !lastRecord.getCanceled())) {
                debtPesos = lastRecord.getBalancePesos() != null ? lastRecord.getBalancePesos() : BigDecimal.ZERO;
                debtDolares = lastRecord.getBalanceDollars() != null ? lastRecord.getBalanceDollars() : BigDecimal.ZERO;
            }

            if (debtPesos.compareTo(BigDecimal.ZERO) > 0 || debtDolares.compareTo(BigDecimal.ZERO) > 0) {
                PatientDebtorDTO dto = new PatientDebtorDTO();
                dto.setId(patient.getId());
                dto.setDni(patient.getDni());
                dto.setFullName(patient.getFullName());
                dto.setDebtPesos(debtPesos);
                dto.setDebtDolares(debtDolares);

                debtors.add(dto);
            }
        }

        return debtors;
    }

    private PatientResponseDTO mapToResponseDTO(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId());
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
            dto.setCreatedByName(patient.getCreatedBy().getName() + " " + patient.getCreatedBy().getLastname());
        }

        return dto;
    }
}