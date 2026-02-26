package com.ciro.backend.controller;

import com.ciro.backend.dto.ReceiptCreateDTO;
import com.ciro.backend.dto.ReceiptResponseDTO;
import com.ciro.backend.service.ReceiptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<ReceiptResponseDTO> createReceipt(
            @RequestBody ReceiptCreateDTO dto) {

        ReceiptResponseDTO response = receiptService.createReceipt(dto);
        return ResponseEntity.ok(response);
    }
}