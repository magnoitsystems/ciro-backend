package com.ciro.backend.controller;

import com.ciro.backend.dto.SupplierCreateDTO;
import com.ciro.backend.dto.SupplierResponseDTO;
import com.ciro.backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAll() {
        return ResponseEntity.ok(supplierService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SupplierResponseDTO> create(@RequestBody SupplierCreateDTO supplierDTO) {
        return new ResponseEntity<>(supplierService.save(supplierDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> update(@PathVariable Long id, @RequestBody SupplierCreateDTO supplierDTO) {
        return ResponseEntity.ok(supplierService.update(supplierDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}