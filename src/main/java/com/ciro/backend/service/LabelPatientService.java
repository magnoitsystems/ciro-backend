package com.ciro.backend.service;

import com.ciro.backend.entity.LabelPatient;
import com.ciro.backend.repository.LabelPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabelPatientService {
    @Autowired
    private LabelPatientRepository labelPatientRepository;

    public LabelPatient assignLabelToPatient(LabelPatient labelPatient) {
        return labelPatientRepository.save(labelPatient);
    }

    public List<LabelPatient> findAll(){
        return labelPatientRepository.findAll();
    }

    public Optional<LabelPatient> findById(Long id){
        if(id == null){
            return null;
        }
        return labelPatientRepository.findById(id);
    }

    public List<LabelPatient> findByPatientId(Long patientId){
        if(patientId == null){
            return null;
        }

        List<LabelPatient> labelPatients = labelPatientRepository.findLabelPatientById(patientId);

        return labelPatients;
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
}
