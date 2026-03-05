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
    private LabelRepository labelRepository;
    @Autowired
    private LabelPatientRepository labelPatientRepository;
    @Autowired
    private PracticeService practiceService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CurrentAccountRepository currentAccountRepository;

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
    public void assignLabel(Long patientId, Long labelId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el paciente con ID "+ patientId));

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label no encontrado"));

        // evitar duplicados
        LabelPatient alreadyExists = labelPatientRepository.existsByPatientIdAndLabelId(patientId, labelId);

        if (alreadyExists != null) {
            throw new DuplicateResourceException("El paciente ya tiene este label");
        }

        LabelPatient patientLabel = new LabelPatient();
        patientLabel.setPatient(patient);
        patientLabel.setLabel(label);

        labelPatientRepository.save(patientLabel);
    }

    public StatisticsDTO getPatientsAndStatistics(Long labelId) {
        labelRepository.findById(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el label con ID "+ labelId));

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
}