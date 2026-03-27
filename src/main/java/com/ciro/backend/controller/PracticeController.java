package com.ciro.backend.controller;

import com.ciro.backend.dto.PracticeDTO;
import com.ciro.backend.entity.Practice;
import com.ciro.backend.service.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/practices")
public class PracticeController {
    @Autowired
    private PracticeService practiceService;

    @GetMapping
    public ResponseEntity<List<Practice>> findAllPractices() {
        List<Practice> practices = practiceService.getAllPractices();
        return ResponseEntity.ok(practices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practice> findPracticeById(@PathVariable Long id){
        Practice practice = practiceService.getPractice(id);
        return ResponseEntity.ok(practice);
    }

    @PostMapping()
    public ResponseEntity<Practice> createPractice(PracticeDTO practiceDTO) {
        Practice practice = practiceService.createPractice(practiceDTO);
        return ResponseEntity.ok(practice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Practice> updatePractice(PracticeDTO practiceDTO, @PathVariable Long id) {
        Practice practice = practiceService.updatePractice(practiceDTO, id);
        return ResponseEntity.ok(practice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePractice(@PathVariable Long id) {
        String response = this.practiceService.deletePractice(id);
        if (response != null) {
            return ResponseEntity.status(500).body("Hubo un error al eliminar la práctica");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type")
    public ResponseEntity<List<Practice>> getPracticesByType(String type) {
        List<Practice> practices = this.practiceService.getPracticesByType(type);
        return ResponseEntity.ok(practices);
    }
}
