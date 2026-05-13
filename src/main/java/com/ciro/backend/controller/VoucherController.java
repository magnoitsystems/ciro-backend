package com.ciro.backend.controller;

import com.ciro.backend.dto.VoucherCreateDTO;
import com.ciro.backend.dto.VoucherDTO;
import com.ciro.backend.dto.VoucherResponseDTO;
import com.ciro.backend.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping
    public ResponseEntity<VoucherResponseDTO> createVoucher(@RequestBody VoucherCreateDTO dto) {
        VoucherResponseDTO response = voucherService.createVoucher(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherDTO>etVoucherById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                voucherService.getVoucherById(id)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VoucherResponseDTO>> getVouchersByPatient(@PathVariable Long patientId) {
        List<VoucherResponseDTO> vouchers = voucherService.getVouchersByPatientId(patientId);
        return ResponseEntity.ok(vouchers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherResponseDTO> updateVoucher(
            @PathVariable Long id,
            @RequestBody VoucherCreateDTO dto) {
        VoucherResponseDTO updatedVoucher = voucherService.updateVoucher(id, dto);
        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }
}