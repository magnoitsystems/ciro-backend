package com.ciro.backend.controller;

import com.ciro.backend.dto.CashBalanceDTO;
import com.ciro.backend.dto.CashMovementDetailDTO;
import com.ciro.backend.dto.RevenueWidgetDTO;
import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.ReportPeriod;
import com.ciro.backend.service.CashMovementService;
import com.ciro.backend.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @GetMapping("/{id}")
    public ResponseEntity<CashMovementDetailDTO> getMovementDetail(@PathVariable Long id) {
        CashMovementDetailDTO detail = cashMovementService.getMovementDetail(id);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/doctor/{doctorId}/incomes")
    public ResponseEntity<List<CashMovement>> getDoctorIncomes(@PathVariable Long doctorId) {
        List<CashMovement> incomes = cashMovementService.getCashMovementsByUserId(doctorId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/balance")
    public ResponseEntity<CashBalanceDTO> getBalance(ReportPeriod period) {
        CashBalanceDTO response = cashMovementService.getCashBalance(period);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/widgets/net-revenue")
    public ResponseEntity<RevenueWidgetDTO> getNetRevenueWidget(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        RevenueWidgetDTO widgetData = cashMovementService.getNetRevenueWidget(startDate, endDate);
        return ResponseEntity.ok(widgetData);
    }
}
