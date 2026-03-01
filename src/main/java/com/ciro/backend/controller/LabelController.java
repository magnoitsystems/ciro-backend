package com.ciro.backend.controller;

import com.ciro.backend.entity.Label;
import com.ciro.backend.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("api/v1/labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping()
    public ResponseEntity<List<Label>> findAll() {
        List<Label> labels = labelService.findAll();

        if(labels == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(labels);
    }

    @GetMapping("{id}")
    public ResponseEntity<Label> findById(@PathVariable Long id) {
        Label label = labelService.findById(id);

        if(label == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(label);
    }

    @PostMapping()
    public ResponseEntity<Label> create(@RequestBody Label label) {
        Label labelSaved = labelService.save(label);

        if(labelSaved == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(labelSaved);
    }

    @PutMapping("{id}")
    public ResponseEntity<Label> update(@PathVariable Long id, @RequestBody Label label) {
        Label labelSaved = labelService.update(label, id);

        if(labelSaved == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(labelSaved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Label> delete(@PathVariable Long id) {
        Label label = labelService.findById(id);

        if(label == null){
            return ResponseEntity.notFound().build();
        }

        labelService.deleteById(id);
        return ResponseEntity.ok(label);
    }
}
