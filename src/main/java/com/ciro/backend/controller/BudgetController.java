package com.ciro.backend.controller;

import com.ciro.backend.dto.BudgetCreateDTO;
import com.ciro.backend.dto.BudgetResponseDTO;
import com.ciro.backend.entity.Budget;
import com.ciro.backend.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/budgets")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @GetMapping()
    public ResponseEntity<List<BudgetResponseDTO>> findAll(){
        List<BudgetResponseDTO> budget = budgetService.findAll();

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> findById(@PathVariable Long id) {
        BudgetResponseDTO budgetDTO = budgetService.findById(id);

        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<BudgetResponseDTO>> findByPatientId(@PathVariable Long id){
        List<BudgetResponseDTO> budget = budgetService.findByPatientId(id);

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<BudgetResponseDTO> createBudget(@ModelAttribute BudgetCreateDTO dto) {
        BudgetResponseDTO newBudget = budgetService.save(dto);
        return new ResponseEntity<>(newBudget, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> updateBudget(@RequestBody BudgetCreateDTO budgetDTO, @PathVariable Long id){
        BudgetResponseDTO response = budgetService.update(id, budgetDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> deleteBudget(@PathVariable Long id){
        BudgetResponseDTO budgetDTO = budgetService.findById(id);

        budgetService.deleteById(id);
        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }
}
