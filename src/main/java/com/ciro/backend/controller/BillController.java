package com.ciro.backend.controller;

import com.ciro.backend.dto.BillCreateDTO;
import com.ciro.backend.dto.BillResponseDTO;
import com.ciro.backend.enums.BillType;
import com.ciro.backend.enums.OriginType;
import com.ciro.backend.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController {

    @Autowired
    private BillService billService;

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
}