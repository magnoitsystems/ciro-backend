package com.ciro.backend.controller;

import com.ciro.backend.dto.BillCreateDTO;
import com.ciro.backend.dto.BillResponseDTO;
import com.ciro.backend.enums.BillType;
import com.ciro.backend.enums.OriginType;
import com.ciro.backend.enums.ReportPeriod;
import com.ciro.backend.service.BillService;
import com.ciro.backend.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private PdfGenerationService pdfGenerationService;

    @PostMapping
    public ResponseEntity<BillResponseDTO> createBill(@RequestBody BillCreateDTO dto) {
        BillResponseDTO response = billService.createBill(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillResponseDTO> updateBill(
            @PathVariable Long id,
            @RequestBody BillCreateDTO dto) {

        BillResponseDTO response = billService.updateBill(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills(
            @RequestParam(required = false) BillType type,
            @RequestParam(required = false) OriginType origin) {

        List<BillResponseDTO> bills = billService.getBills(type, origin);
        return ResponseEntity.ok(bills);
    }

    @GetMapping(value = "/report/pdf", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadBillsReport(
            @RequestParam ReportPeriod period,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {

        List<BillResponseDTO> paidBills = billService.getPaidBillsForReport(period, date);

        String reportTitle = "Reporte de Gastos Pagados - ";
        LocalDate refDate = (date != null) ? date : LocalDate.now();
        switch (period) {
            case DAY: reportTitle += "Día " + refDate.toString(); break;
            case WEEK: reportTitle += "Semana del " + refDate.with(java.time.DayOfWeek.MONDAY).toString(); break;
            case MONTH: reportTitle += "Mes " + refDate.getMonthValue() + "/" + refDate.getYear(); break;
        }

        byte[] pdfBytes = pdfGenerationService.generateExpenseReportPdf(paidBills, reportTitle);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_gastos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}