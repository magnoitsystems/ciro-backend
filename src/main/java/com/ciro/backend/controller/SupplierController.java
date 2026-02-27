package com.ciro.backend.controller;

import com.ciro.backend.dto.SupplierDTO;
import com.ciro.backend.entity.Supplier;
import com.ciro.backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping()
    public ResponseEntity<List<SupplierDTO>> getAll() {
        List<SupplierDTO> supplierDTO = supplierService.findAll();
        if(supplierDTO == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(supplierDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getById(@PathVariable Long id) {
        SupplierDTO supplierDTO = supplierService.findById(id);
        if(supplierDTO == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(supplierDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Supplier> create(@RequestBody SupplierDTO supplierDTO) {
        Supplier newSupplier = supplierService.save(supplierDTO);

        if(newSupplier == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newSupplier, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable Long id, @RequestBody SupplierDTO supplierDTO) {
        Supplier editedSupplier = supplierService.update(supplierDTO, id);

        if(editedSupplier == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(editedSupplier, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SupplierDTO> delete(@PathVariable Long id) {
        SupplierDTO supplier = supplierService.findById(id);
        if(supplier != null){
            supplierService.deleteById(id);
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
