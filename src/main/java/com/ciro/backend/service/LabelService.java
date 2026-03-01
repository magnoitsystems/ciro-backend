package com.ciro.backend.service;

import com.ciro.backend.entity.Label;
import com.ciro.backend.repository.LabelRepository;
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
            return labelRepository.findById(id).orElse(null);
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
        Label label1 = findById(id);

        if(label.getLabel() != null){
            label1.setLabel(label.getLabel());
            return labelRepository.save(label1);
        }
        return null;
    }
}
