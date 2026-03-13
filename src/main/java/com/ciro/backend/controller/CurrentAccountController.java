package com.ciro.backend.controller;

import com.ciro.backend.dto.CurrentAccountResponseDTO;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.service.CurrentAccountService;
import com.ciro.backend.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/current-accounts")
public class CurrentAccountController {

    @Autowired
    private CurrentAccountService currentAccountService;
    @Autowired
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/patient/{patientId}/pdf")
    public ResponseEntity<byte[]> getPatientCurrentAccountPdf(
            @PathVariable Long patientId,
            @RequestParam(required = false) CurrentAccountType type) {

        CurrentAccountResponseDTO account = currentAccountService.getPatientCurrentAccount(patientId, type);

        byte[] pdfBytes = pdfGenerationService.generateCurrentAccountPdf(account);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        String filename = "Cuenta_Corriente_" + account.getPatientFullName().replace(" ", "_") + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<CurrentAccountResponseDTO> getPatientCurrentAccount(
            @PathVariable Long patientId,
            @RequestParam(required = false) CurrentAccountType type) { // ¡El nuevo parámetro!

        CurrentAccountResponseDTO account = currentAccountService.getPatientCurrentAccount(patientId, type);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/patient/{patientId}/cancel-debt")
    public ResponseEntity<String> cancelLastMovement(@PathVariable Long patientId) {
        currentAccountService.cancelPatientDebt(patientId);
        return ResponseEntity.ok("El último movimiento fue cancelado exitosamente.");
    }
}