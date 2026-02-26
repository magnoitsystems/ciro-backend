package com.ciro.backend.controller;

import com.ciro.backend.dto.VoucherCreateDTO;
import com.ciro.backend.dto.VoucherResponseDTO;
import com.ciro.backend.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}