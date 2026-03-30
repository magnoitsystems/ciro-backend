package com.ciro.backend.controller;

import com.ciro.backend.dto.*;
import com.ciro.backend.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tariffs")
public class TariffController {

    @Autowired
    private TariffService tariffService;

    @PostMapping
    public ResponseEntity<TariffResponseDTO> createTariff(@RequestBody TariffCreateDTO dto) {
        return new ResponseEntity<>(tariffService.createTariff(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TariffResponseDTO>> getTariffs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPesos,
            @RequestParam(required = false) BigDecimal minDollars) {
        return ResponseEntity.ok(tariffService.getTariffs(keyword, minPesos, minDollars));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TariffResponseDTO> updateTariff(@PathVariable Long id, @RequestBody TariffUpdateDTO dto) {
        return ResponseEntity.ok(tariffService.updateTariff(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariff(id);
        return ResponseEntity.noContent().build();
    }
}