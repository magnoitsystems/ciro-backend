package com.ciro.backend.service;

import com.ciro.backend.dto.PracticeDTO;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.Practice;
import com.ciro.backend.entity.User;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.PracticeRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PracticeService {
    @Autowired
    private PracticeRepository practiceRepository;

    public Practice createPractice(PracticeDTO practiceDTO) {
        Practice practice = new Practice();

        practice.setPracticeDate(practiceDTO.getPracticeDate());
        practice.setPracticeType(practiceDTO.getPracticeType());

        if(practiceDTO.getAmountDollars() != null && practiceDTO.getTc() != null) {
            practice.setAmountDollars(practiceDTO.getAmountDollars());
            practice.setTc(practiceDTO.getTc());
            BigDecimal amountPesos = practiceDTO.getAmountDollars().multiply(practiceDTO.getTc());
            practice.setAmountPesos(amountPesos);
        } else if (practiceDTO.getAmountPesos() != null && practiceDTO.getTc() != null) {
            practice.setAmountPesos(practiceDTO.getAmountPesos());
            practice.setTc(practiceDTO.getTc());
            BigDecimal amountDollars = practiceDTO.getAmountPesos().multiply(practiceDTO.getTc());
            practice.setAmountDollars(amountDollars);
        }

        return practiceRepository.save(practice);
    }

    public Practice getPractice(Long id) {
        return practiceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La práctica " + id + " no encontrado"));
    }

    public List<Practice> getAllPractices() {
        return practiceRepository.findAll();
    }

    public Practice updatePractice(PracticeDTO practiceDTO, Long id) {
        if(id >= 0){
            Practice practice = practiceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Práctica con el ID: " + id + "  no encontrada"));
            if(practiceDTO.getPracticeDate() != null) {
                practice.setPracticeDate(practiceDTO.getPracticeDate());
            }

            if(practiceDTO.getPracticeType() != null) {
                practice.setPracticeType(practiceDTO.getPracticeType());
            }

            if(practiceDTO.getAmountDollars() != null) {
                practice.setAmountDollars(practiceDTO.getAmountDollars());
            }

            if (practiceDTO.getAmountPesos() != null) {
                practice.setAmountPesos(practiceDTO.getAmountPesos());
            }

            if(practiceDTO.getTc() != null) {
                practice.setTc(practiceDTO.getTc());
            }

            return practiceRepository.save(practice);
        }
        return null;
    }

    public PracticeDTO mapToDTO(Practice practice) {
        PracticeDTO practiceDTO = new PracticeDTO();

        practiceDTO.setPracticeDate(practice.getPracticeDate());
        practiceDTO.setPracticeType(practice.getPracticeType());
        return practiceDTO;
    }

    public String deletePractice(Long id) {
        Practice practice = this.practiceRepository.findById(id).orElse(null);
        if(practice != null) {
            practiceRepository.delete(practice);
            return "Práctica con el ID: " + practice.getId()+" eliminada correctamente";
        }
        return null;
    }

    public List<Practice> getPracticesByType(String type) {
        return this.practiceRepository.getPracticesByType(type);
    }
}
