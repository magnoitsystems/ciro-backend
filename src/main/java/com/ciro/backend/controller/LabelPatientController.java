package com.ciro.backend.controller;

import com.ciro.backend.entity.LabelPatient;
import com.ciro.backend.repository.LabelPatientRepository;
import com.ciro.backend.service.LabelPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("api/v1/labelPatients")
public class LabelPatientController {
    @Autowired
    private LabelPatientService labelPatientService;

    @GetMapping()
    public ResponseEntity<List<LabelPatient>> findAll(){
        List<LabelPatient> labelPatients = labelPatientService.findAll();

        if(labelPatients == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(labelPatients);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LabelPatient> findById(@PathVariable Long id){
        Optional<LabelPatient> labelPatient = labelPatientService.findById(id);

        return labelPatient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/patient/{idPatient}/label/{idLabel}")
    public ResponseEntity<LabelPatient> findByPatientIdAndLabelId(@PathVariable Long idPatient, @PathVariable Long idLabel){
        LabelPatient labelPatient = labelPatientService.findByPatientIdAndLabelId(idPatient, idLabel);

        if(labelPatient == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(labelPatient);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<LabelPatient>> findByPatientId(@PathVariable Long id){
        List<LabelPatient> labelPatients = labelPatientService.findByPatientId(id);

        if(labelPatients == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(labelPatients);
    }

    @GetMapping("/label/{id}")
    public ResponseEntity<List<LabelPatient>> findByLabelId(@PathVariable Long id){
        List<LabelPatient> labelPatients = labelPatientService.findByLabelId(id);

        if(labelPatients == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(labelPatients);
    }

    @PostMapping()
    public ResponseEntity<LabelPatient> savePatient(@RequestBody LabelPatient labelPatient){
        LabelPatient newLabelPatient = labelPatientService.assignLabelToPatient(labelPatient);

        if(newLabelPatient == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(newLabelPatient);
    }
}
