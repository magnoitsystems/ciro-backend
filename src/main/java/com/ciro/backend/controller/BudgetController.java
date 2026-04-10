package com.ciro.backend.controller;

import com.ciro.backend.dto.BudgetCreateDTO;
import com.ciro.backend.dto.BudgetResponseDTO;
import com.ciro.backend.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>> findAll() {
        List<BudgetResponseDTO> budgets = budgetService.findAll();
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> findById(@PathVariable Long id) {
        BudgetResponseDTO budgetDTO = budgetService.findById(id);
        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<BudgetResponseDTO>> findByPatientId(@PathVariable Long id) {
        List<BudgetResponseDTO> budgets = budgetService.findByPatientId(id);
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<BudgetResponseDTO> createBudget(@ModelAttribute BudgetCreateDTO dto) {
        BudgetResponseDTO newBudget = budgetService.save(dto);
        return new ResponseEntity<>(newBudget, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<BudgetResponseDTO> updateBudget(@ModelAttribute BudgetCreateDTO budgetDTO, @PathVariable Long id) {
        BudgetResponseDTO response = budgetService.update(id, budgetDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> deleteBudget(@PathVariable Long id) {
        BudgetResponseDTO budgetDTO = budgetService.findById(id);
        budgetService.deleteById(id);
        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }
}