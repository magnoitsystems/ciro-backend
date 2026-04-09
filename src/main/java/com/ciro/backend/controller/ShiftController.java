package com.ciro.backend.controller;

import com.ciro.backend.dto.ShiftCreateDTO;
import com.ciro.backend.dto.ShiftResponseDTO;
import com.ciro.backend.dto.ShiftWidgetDTO;
import com.ciro.backend.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @GetMapping
    public ResponseEntity<List<ShiftResponseDTO>> getAllShifts() {
        return ResponseEntity.ok(shiftService.getAllShift());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftResponseDTO> getShift(@PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<ShiftResponseDTO>> getAllShiftsByDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getAllShiftByDoctor(id));
    }

    @GetMapping("/patient/{dni}")
    public ResponseEntity<List<ShiftResponseDTO>> getAllShiftsByPatient(@PathVariable String dni) {
        return ResponseEntity.ok(shiftService.getAllShiftByPatient(dni));
    }

    @PostMapping // ¡Acá está el fix vital, un solo body!
    public ResponseEntity<ShiftResponseDTO> createShift(@RequestBody ShiftCreateDTO dto) {
        return new ResponseEntity<>(shiftService.createShift(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftResponseDTO> updateShift(@PathVariable Long id, @RequestBody ShiftCreateDTO dto) {
        return ResponseEntity.ok(shiftService.updateShift(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/range")
    public ResponseEntity<List<ShiftResponseDTO>> getShiftsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<ShiftResponseDTO> shifts = shiftService.getShiftsByDateRange(startDate, endDate);
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/widget/dashboard-info")
    public ResponseEntity<ShiftWidgetDTO> getShiftDashboardInfo() {
        return ResponseEntity.ok(shiftService.getDashboardWidgetData());
    }
}