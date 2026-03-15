package com.ciro.backend.controller;

import com.ciro.backend.dto.NoteDTO;
import com.ciro.backend.dto.ShiftDTO;
import com.ciro.backend.service.ShiftService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        return new ResponseEntity<>(shiftDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftDTO> getShift(@PathVariable Long id) {
        ShiftDTO shiftDTO = shiftService.getShiftById(id);

        return new ResponseEntity<>(shiftDTO, HttpStatus.OK);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<ShiftDTO>> getAllShiftByDoctor(@PathVariable Long id) {
        List<ShiftDTO> shiftDTO = shiftService.getAllShiftByDoctor(id);

        return new ResponseEntity<>(shiftDTO, HttpStatus.OK);
    }

    @GetMapping("/patient/{dni}")
    public ResponseEntity<List<ShiftDTO>> getAllShiftByPatient(@PathVariable String dni) {
        List<ShiftDTO> shifts = shiftService.getAllShiftByPatient(dni);

        return ResponseEntity.ok(shifts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ShiftDTO> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping()
    public ResponseEntity<ShiftDTO> createShift(@RequestBody ShiftDTO shiftDTO,  @Nullable @RequestBody NoteDTO noteDTO) {
        shiftService.createShift(shiftDTO, noteDTO);

        return new ResponseEntity<>(shiftDTO, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ShiftDTO> updateShift(@PathVariable Long id, @RequestBody ShiftDTO shiftDTO) {
        shiftService.updateShift(shiftDTO, id);

        return new ResponseEntity<>(shiftDTO, HttpStatus.OK);
    }
}
