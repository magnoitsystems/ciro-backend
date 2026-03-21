package com.ciro.backend.controller;

import com.ciro.backend.dto.CashMovementDetailDTO;
import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.ReportPeriod;
import com.ciro.backend.service.CashMovementService;
import com.ciro.backend.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cash-movements")
public class CashMovementController {

    @Autowired private CashMovementService cashMovementService;
    @Autowired private PdfGenerationService pdfGenerationService;

    @GetMapping
    public ResponseEntity<List<CashMovement>> getCashMovements(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) ReportPeriod period) {

        return ResponseEntity.ok(cashMovementService.getCashMovements(doctorId, period));
    }

    @GetMapping("/report/pdf")
    public ResponseEntity<byte[]> downloadCashReport(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) ReportPeriod period) {

        List<CashMovement> movements = cashMovementService.getCashMovements(doctorId, period);

        String title = "Reporte de Caja - " + (period != null ? period.name() : "HISTÓRICO");
        if (doctorId != null) title += " (Doctor ID: " + doctorId + ")";

        byte[] pdf = pdfGenerationService.generateCashReportPdf(movements, title);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_caja.pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @PatchMapping("/{id}/assign-doctor")
    public ResponseEntity<Void> assignDoctor(@PathVariable Long id, @RequestParam Long doctorId) {
        cashMovementService.assignDoctor(id, doctorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashMovementDetailDTO> getMovementDetail(@PathVariable Long id) {
        CashMovementDetailDTO detail = cashMovementService.getMovementDetail(id);
        return ResponseEntity.ok(detail);
    }
}
