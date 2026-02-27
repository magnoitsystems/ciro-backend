package com.ciro.backend.controller;

import com.ciro.backend.dto.CurrentAccountResponseDTO;
import com.ciro.backend.service.CurrentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/current-accounts")
public class CurrentAccountController {

    @Autowired
    private CurrentAccountService currentAccountService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<CurrentAccountResponseDTO> getPatientCurrentAccount(@PathVariable Long patientId) {
        CurrentAccountResponseDTO account = currentAccountService.getPatientCurrentAccount(patientId);
        return ResponseEntity.ok(account);
    }
}