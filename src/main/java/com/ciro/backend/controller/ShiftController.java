package com.ciro.backend.controller;

import com.ciro.backend.dto.ShiftDTO;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    @GetMapping()
    public ResponseEntity<List<ShiftDTO>> getAllShift() {
        List<ShiftDTO> shiftDTOs = shiftService.getAllShift();
        if (shiftDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shiftDTOs, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ShiftDTO> getShift(@PathVariable Long id) {
        ShiftDTO shiftDTO = shiftService.getShiftById(id);
        if (shiftDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shiftDTO, HttpStatus.OK);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<ShiftDTO>> getAllShiftByDoctor(@PathVariable Long id) {
        if(id >= 0) {
            List<ShiftDTO> shiftDTO = shiftService.getAllShiftByDoctor(id);

            if(shiftDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(shiftDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/patient/{dni}")
    public ResponseEntity<List<ShiftDTO>> getAllShiftByPatient(@PathVariable String dni) {
        List<ShiftDTO> shifts = shiftService.getAllShiftByPatient(dni);
        if (shifts == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shifts);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ShiftDTO> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping()
    public ResponseEntity<ShiftDTO> createShift(@RequestBody ShiftDTO shiftDTO) {
        Shift newShif = shiftService.createShift(shiftDTO);
        if (newShif == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(shiftDTO, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ShiftDTO> updateShift(@PathVariable Long id, @RequestBody ShiftDTO shiftDTO) {
        Shift shift = shiftService.updateShift(shiftDTO, id);
        if (shift == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shiftDTO, HttpStatus.OK);
    }
}
