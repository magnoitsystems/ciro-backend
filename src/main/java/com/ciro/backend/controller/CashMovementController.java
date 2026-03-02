package com.ciro.backend.controller;

import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.ReportPeriod;
import com.ciro.backend.service.CashMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cash-movements")
public class CashMovementController {

    @Autowired private CashMovementService cashMovementService;

    @GetMapping("/report")
    public ResponseEntity<List<CashMovement>> getReporteCaja(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) ReportPeriod period) {

        return ResponseEntity.ok(cashMovementService.obtenerCaja(doctorId, period));
    }

    @PatchMapping("/{id}/assign-doctor")
    public ResponseEntity<Void> asignarDoctor(@PathVariable Long id, @RequestParam Long doctorId) {
        cashMovementService.asignarDoctorAMovimiento(id, doctorId);
        return ResponseEntity.noContent().build();
    }
}
