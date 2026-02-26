package com.ciro.backend.service;

import com.ciro.backend.dto.ShiftDTO;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.User;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.ShiftRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    public List<ShiftDTO> getAllShift() {
        List<Shift> shifts = shiftRepository.findAll();
        List<ShiftDTO> shiftDTOs = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftDTOs.add(mapToDTO(shift));
        }
        return shiftDTOs;
    }

    public ShiftDTO getShiftById(Long id) {
        if(id >= 0){
                Shift shift = shiftRepository.findById(id).orElseThrow( () -> new RuntimeException("Turno no encontrado"));
                return mapToDTO(shift);
        }
        return null;
    }

    public Shift createShift(ShiftDTO shiftDTO) {
        Patient patient = patientRepository.findByDni(shiftDTO.getPatient().getDni());

        User doctor = userRepository.findById(shiftDTO.getDoctor().getId()).orElseThrow( () -> new RuntimeException("Doctor no existe"));

        if(patient != null && doctor != null){
            Shift newShift = new Shift();
            newShift.setPatient(patient);
            newShift.setDoctor(doctor);
            newShift.setStatus(shiftDTO.getStatus());
            newShift.setShiftDate(shiftDTO.getShiftDate());

            return shiftRepository.save(newShift);
        }
        return null;
    }

    public void deleteShift(Long id) {
        if(id >= 0){
            shiftRepository.deleteById(id);
        }
    }

    public Shift updateShift(ShiftDTO shiftDTO, Long id) {
        if(id >= 0) {
            Shift shift = shiftRepository.findById(id).orElseThrow( () -> new RuntimeException("Shift no existe"));

            if(shiftDTO.getShiftDate() != null){
                shift.setShiftDate(shiftDTO.getShiftDate());
            }
            if(shiftDTO.getPatient() != null){
                shift.setPatient(shiftDTO.getPatient());
            }
            if(shiftDTO.getDoctor() != null){
                shift.setDoctor(shiftDTO.getDoctor());
            }
            if(shiftDTO.getStatus() != null){
                shift.setStatus(shiftDTO.getStatus());
            }
            return shiftRepository.save(shift);
        }
        return null;
    }

    public List<ShiftDTO> getAllShiftByPatient(String dni) {
        List<Shift> shifts = shiftRepository.getByPatient(dni);
        List<ShiftDTO> shiftDTOs = new ArrayList<>();

        for (Shift shift : shifts) {
            shiftDTOs.add(mapToDTO(shift));
        }
        return shiftDTOs;
    }

    public List<ShiftDTO> getAllShiftByDoctor(Long id) {
        List<Shift> shifts = shiftRepository.getByDoctor(id);
        List<ShiftDTO> shiftDTOs = new ArrayList<>();

        for (Shift shift : shifts) {
            shiftDTOs.add(mapToDTO(shift));
        }
        return shiftDTOs;
    }

    public ShiftDTO mapToDTO(Shift shift) {
        ShiftDTO dto = new ShiftDTO();
        dto.setDoctor(shift.getDoctor());
        dto.setShiftDate(shift.getShiftDate());
        dto.setStatus(shift.getStatus());
        dto.setPatient(shift.getPatient());

        return dto;
    }
}
