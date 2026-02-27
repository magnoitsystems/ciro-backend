package com.ciro.backend.service;

import com.ciro.backend.dto.BudgetDTO;
import com.ciro.backend.entity.Budget;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.BudgetRepository;
import com.ciro.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private PatientRepository patientRepository;

    public List<BudgetDTO> findByPatientId(Long patientId) {
        if(patientId >= 0){
            Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte médico con ID: " + patientId));
            List<Budget> budget = budgetRepository.findByPatientId(patient.getId());
            List<BudgetDTO> budgetDTOList = new ArrayList<>();

            for(Budget budgetDTO : budget){
                budgetDTOList.add(mapToDTO(budgetDTO));
            }
            return budgetDTOList;
        }
        return null;
    }

    public Budget save(BudgetDTO budgetDTO) {
        Budget budget = new Budget();

        Patient patient = patientRepository.findById(budgetDTO.getPatient().getId()).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        if(patient != null){
            budget.setPatient(patient);
        }

        budget.setFile_url(budgetDTO.getFile_url());
        budget.setUploadedDate(budgetDTO.getUploadedDate());

        return budgetRepository.save(budget);
    }

    public BudgetDTO findById(Long id) {
        if(id >= 0){
            Budget budget = budgetRepository.findById(id).orElse(null);

            if(budget != null){
                return mapToDTO(budget);
            }
            return null;
        }
        return null;
    }

    public List<BudgetDTO> findAll() {
        List<Budget> budgets = (List<Budget>) budgetRepository.findAll();
        List<BudgetDTO> budgetDTOList = new ArrayList<>();

        for(Budget budget : budgets){
            budgetDTOList.add(mapToDTO(budget));
        }

        return budgetDTOList;
    }

    public Budget update(BudgetDTO budgetDTO, Long id) {

        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Presupuesto no encontrado"));

        if(budgetDTO.getPatient() != null) {
            Patient patient = patientRepository.findById(budgetDTO.getPatient().getId()).orElse(null);
            budget.setPatient(patient);
        }

        if(budgetDTO.getUploadedDate() != null){
            budget.setUploadedDate(budgetDTO.getUploadedDate());
        }

        if(budgetDTO.getFile_url() != null){
            budget.setFile_url(budgetDTO.getFile_url());
        }

        return budgetRepository.save(budget);
    }

    public void deleteById(Long id) {
        if(id >= 0){
            budgetRepository.deleteById(id);
        }
    }

    public BudgetDTO mapToDTO(Budget budget) {
        BudgetDTO dto = new BudgetDTO();

        dto.setFile_url(budget.getFile_url());
        dto.setPatient(budget.getPatient());
        dto.setUploadedDate(budget.getUploadedDate());

        return dto;
    }
}
