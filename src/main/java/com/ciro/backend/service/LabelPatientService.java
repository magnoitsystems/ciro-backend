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
import java.util.Optional;

@Service
public class LabelPatientService {

    @Autowired
    private LabelPatientRepository labelPatientRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private LabelRepository labelRepository;

    public LabelPatient assignLabelToPatient(LabelPatient labelPatient) {
        return labelPatientRepository.save(labelPatient);
    }

    public List<LabelPatient> findAll(){
        return labelPatientRepository.findAll();
    }

    public Optional<LabelPatient> findById(Long id){
        if(id == null){
            return Optional.empty();
        }
        return labelPatientRepository.findById(id);
    }

    public List<LabelPatient> findByPatientId(Long patientId){
        if(patientId == null){
            return null;
        }

        return labelPatientRepository.findLabelPatientById(patientId);
    }

    public List<LabelPatient> findByLabelId(Long labelId){
        if(labelId == null){
            return null;
        }

        return labelPatientRepository.findLabelPatientByLabel(labelId);
    }

    public LabelPatient findByPatientIdAndLabelId(Long patientId, Long labelId){
        if(patientId == null || labelId == null){
            return null;
        }

        return labelPatientRepository.existsByPatientIdAndLabelId(patientId, labelId);
    }

    @Transactional
    public void assignLabel(Long patientId, Long labelId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el paciente con ID "+ patientId));

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label no encontrado"));

        LabelPatient alreadyExists = labelPatientRepository.existsByPatientIdAndLabelId(patientId, labelId);

        if (alreadyExists != null) {
            throw new DuplicateResourceException("El paciente ya tiene este label");
        }

        LabelPatient patientLabel = new LabelPatient();
        patientLabel.setPatient(patient);
        patientLabel.setLabel(label);

        labelPatientRepository.save(patientLabel);
    }

}
