package com.ciro.backend.service;

import com.ciro.backend.dto.BudgetCreateDTO;
import com.ciro.backend.dto.BudgetResponseDTO;
import com.ciro.backend.entity.Budget;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.BudgetRepository;
import com.ciro.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<BudgetResponseDTO> findByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        List<Budget> budgets = budgetRepository.findByPatientId(patient.getId());
        return budgets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public BudgetResponseDTO save(BudgetCreateDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        Budget budget = new Budget();
        budget.setPatient(patient);
        budget.setUploadedDate(dto.getUploadedDate() != null ? dto.getUploadedDate() : LocalDate.now());

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                String fileUrl = cloudinaryService.uploadFile(dto.getFile());
                budget.setFile_url(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
            }
        }

        Budget savedBudget = budgetRepository.save(budget);
        return mapToDTO(savedBudget);
    }

    public BudgetResponseDTO findById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El presupuesto con id " + id + " no existe"));
        return mapToDTO(budget);
    }

    public List<BudgetResponseDTO> findAll() {
        List<Budget> budgets = (List<Budget>) budgetRepository.findAll();
        return budgets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public BudgetResponseDTO update(Long id, BudgetCreateDTO dto) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto no encontrado con id: " + id));

        if (dto.getPatientId() != null) {
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
            budget.setPatient(patient);
        }

        if (dto.getUploadedDate() != null) {
            budget.setUploadedDate(dto.getUploadedDate());
        }

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                String fileUrl = cloudinaryService.uploadFile(dto.getFile());
                budget.setFile_url(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
            }
        }

        Budget updatedBudget = budgetRepository.save(budget);
        return mapToDTO(updatedBudget);
    }

    @Transactional
    public void deleteById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto no encontrado"));
        budgetRepository.delete(budget);
    }

    private BudgetResponseDTO mapToDTO(Budget budget) {
        BudgetResponseDTO dto = new BudgetResponseDTO();
        dto.setId(budget.getId());
        dto.setFileUrl(budget.getFile_url());
        dto.setUploadedDate(budget.getUploadedDate());

        if (budget.getPatient() != null) {
            dto.setPatientId(budget.getPatient().getId());
            dto.setPatientFullName(budget.getPatient().getFullName());
        }
        return dto;
    }
}