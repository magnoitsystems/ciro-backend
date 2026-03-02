package com.ciro.backend.controller;

import com.ciro.backend.dto.BudgetDTO;
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
    public ResponseEntity<List<BudgetDTO>> findAll(){
        List<BudgetDTO> budget = budgetService.findAll();

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<BudgetDTO> findById(@PathVariable Long id) {
        BudgetDTO budgetDTO = budgetService.findById(id);

        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<BudgetDTO>> findByPatientId(@PathVariable Long id){
        List<BudgetDTO> budget = budgetService.findByPatientId(id);

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Budget> addBudget(@RequestBody BudgetDTO budgetDTO){
        Budget newBudget = budgetService.save(budgetDTO);

        return new ResponseEntity<>(newBudget, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@RequestBody BudgetDTO budgetDTO, @PathVariable Long id){
        budgetService.update(budgetDTO, id);

        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BudgetDTO> deleteBudget(@PathVariable Long id){
        BudgetDTO budgetDTO = budgetService.findById(id);

        budgetService.deleteById(id);
        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }
}
