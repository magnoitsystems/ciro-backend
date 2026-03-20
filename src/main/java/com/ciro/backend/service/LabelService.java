package com.ciro.backend.service;

import com.ciro.backend.entity.Label;
import com.ciro.backend.entity.LabelPatient;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.exception.DuplicateResourceException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.LabelPatientRepository;
import com.ciro.backend.repository.LabelRepository;
import com.ciro.backend.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    public List<Label> findAll() {
        return labelRepository.findAll();
    }

    public Label findById(Long id) {
        if(id >= 0){
            return labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Label " + id + " no encontrado"));
        }
        return null;
    }

    public Label save(Label label) {
        return labelRepository.save(label);
    }

    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }

    public Label update(Label label, Long id) {
        Label label1 = labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Label " + id + " no encontrado"));

        if(label.getLabel() != null){
            label1.setLabel(label.getLabel());
            return labelRepository.save(label1);
        }
        return null;
    }

    public Label getOrCreateLabel(String labelText) {
        if (labelText == null || labelText.trim().isEmpty()) {
            return null;
        }
        String cleanLabel = labelText.trim();
        return labelRepository.findByLabel(cleanLabel)
                .orElseGet(() -> {
                    Label newLabel = new Label();
                    newLabel.setLabel(cleanLabel);
                    return labelRepository.save(newLabel);
                });
    }
}
