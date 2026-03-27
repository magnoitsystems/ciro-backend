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

import java.util.ArrayList;
import java.util.List;

@Service
public class PracticeService {
    @Autowired
    private PracticeRepository practiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;

    public Practice createPractice(PracticeDTO practiceDTO) {
        Practice practice = new Practice();

        User doctor = userRepository.findById(practiceDTO.getDoctor().getId()).orElseThrow(() -> new ResourceNotFoundException("El doctor " + practiceDTO.getDoctor().getId() + " no encontrado"));
        Patient patient = patientRepository.findById(practiceDTO.getPatient().getId()).orElseThrow(() -> new ResourceNotFoundException("El paciente " + practiceDTO.getPatient().getId() + " no encontrado"));

        practice.setPracticeDate(practiceDTO.getPracticeDate());
        practice.setAmount(practiceDTO.getAmount());
        practice.setDoctor(doctor);
        practice.setPatient(patient);
        practice.setReimplantation(practiceDTO.getReimplantation());
        practice.setPracticeType(practiceDTO.getPracticeType());

        return practiceRepository.save(practice);
    }

    public PracticeDTO getPractice(Long id) {
            Practice practice = practiceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La práctica " + id + " no encontrado"));
            return mapToDTO(practice);
    }

    public List<PracticeDTO> getAllPractices() {
        List<Practice> practices = practiceRepository.findAll();
        List<PracticeDTO> practiceDTOs = new ArrayList<>();

        for (Practice practice : practices) {
            practiceDTOs.add(mapToDTO(practice));
        }

        return practiceDTOs;
    }

    public Practice updatePractice(PracticeDTO practiceDTO, Long id) {
        if(id >= 0){
            Practice practice = practiceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La práctica " + id + " no encontrado"));
            if(practiceDTO.getPracticeDate() != null) {
                practice.setPracticeDate(practiceDTO.getPracticeDate());
            }

            if(practiceDTO.getAmount() != null) {
                practice.setAmount(practiceDTO.getAmount());
            }

            if(practiceDTO.getDoctor() != null) {
                User doctor = userRepository.findById(practiceDTO.getDoctor().getId()).orElseThrow(() -> new ResourceNotFoundException("El doctor " + practiceDTO.getDoctor().getId() + " no encontrado"));
                practice.setDoctor(doctor);
            }

            if(practiceDTO.getPatient() != null) {
                Patient patient = patientRepository.findById(practiceDTO.getPatient().getId()).orElseThrow(() -> new ResourceNotFoundException("El paciente " + practiceDTO.getPatient().getId() + " no encontrado"));
                practice.setPatient(patient);
            }

            if(practiceDTO.getReimplantation() != null) {
                practice.setReimplantation(practiceDTO.getReimplantation());
            }

            if(practiceDTO.getPracticeType() != null) {
                practice.setPracticeType(practiceDTO.getPracticeType());
            }

            return practiceRepository.save(practice);
        }
        return null;
    }

    public List<PracticeDTO> getPracticesByDoctor(Long id) {
        User doctor = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El doctor " + id + " no encontrado"));
        List<Practice> practicesDTOs = practiceRepository.findPracticeByDoctorId(doctor.getId());
        List<PracticeDTO> practiceDTOList = new ArrayList<>();

        for (Practice practice : practicesDTOs) {
            practiceDTOList.add(mapToDTO(practice));
        }

        return practiceDTOList;
    }

    public List<PracticeDTO> getPracticesByPatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El doctor " + id + " no encontrado"));
        List<Practice> practicesDTOs = practiceRepository.findPracticeByPatientId(patient.getId());
        List<PracticeDTO> practiceDTOList = new ArrayList<>();

        for (Practice practice : practicesDTOs) {
            practiceDTOList.add(mapToDTO(practice));
        }

        return practiceDTOList;
    }

    public PracticeDTO mapToDTO(Practice practice) {
        PracticeDTO practiceDTO = new PracticeDTO();

        practiceDTO.setAmount(practice.getAmount());
        practiceDTO.setPracticeDate(practice.getPracticeDate());
        practiceDTO.setDoctor(practice.getDoctor());
        practiceDTO.setPatient(practice.getPatient());
        practiceDTO.setReimplantation(practice.getReimplantation());
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
}
