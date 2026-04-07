package com.ciro.backend.service;

import com.ciro.backend.dto.ShiftCreateDTO;
import com.ciro.backend.dto.ShiftResponseDTO;
import com.ciro.backend.dto.NoteDTO;
import com.ciro.backend.entity.Note;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.User;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.NoteRepository;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.ShiftRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;

    public List<ShiftResponseDTO> getAllShift() {
        return shiftRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ShiftResponseDTO getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));
        return mapToDTO(shift);
    }

    public List<ShiftResponseDTO> getAllShiftByPatient(String dni) {
        return shiftRepository.getByPatient(dni).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ShiftResponseDTO> getAllShiftByDoctor(Long doctorId) {
        return shiftRepository.getByDoctor(doctorId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ShiftResponseDTO> getShiftsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Shift> shifts = shiftRepository.findByShiftDateBetween(startDate, endDate);
        return shifts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ShiftResponseDTO createShift(ShiftCreateDTO dto) {
        Patient patient = patientRepository.findByDni(dto.getPatientDni());
        if (patient == null) {
            throw new ResourceNotFoundException("Paciente no encontrado con DNI: " + dto.getPatientDni());
        }

        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no existe"));

        Shift newShift = new Shift();
        newShift.setPatient(patient);
        newShift.setDoctor(doctor);
        newShift.setStatus(dto.getStatus());
        newShift.setShiftDate(dto.getShiftDate());

        Shift savedShift = shiftRepository.save(newShift);

        if (dto.getNoteDescription() != null && !dto.getNoteDescription().isEmpty()) {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setDescription(dto.getNoteDescription());
            noteDTO.setShift(savedShift);
            noteDTO.setDate(dto.getShiftDate());
            noteService.createNote(noteDTO);
        }

        return mapToDTO(savedShift);
    }

    @Transactional
    public ShiftResponseDTO updateShift(Long id, ShiftCreateDTO dto) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));

        if (dto.getShiftDate() != null) shift.setShiftDate(dto.getShiftDate());
        if (dto.getStatus() != null) shift.setStatus(dto.getStatus());

        if (dto.getPatientDni() != null) {
            Patient patient = patientRepository.findByDni(dto.getPatientDni());
            if (patient != null) shift.setPatient(patient);
        }
        if (dto.getDoctorId() != null) {
            User doctor = userRepository.findById(dto.getDoctorId()).orElse(shift.getDoctor());
            shift.setDoctor(doctor);
        }

        Shift updatedShift = shiftRepository.save(shift);

        if (dto.getNoteDescription() != null) {
            noteRepository.findByShiftId(updatedShift.getId()).ifPresentOrElse(
                    existingNote -> {
                        existingNote.setDescription(dto.getNoteDescription());
                        noteRepository.save(existingNote);
                    },
                    () -> {
                        if (!dto.getNoteDescription().isEmpty()) {
                            Note newNote = new Note();
                            newNote.setDescription(dto.getNoteDescription());
                            newNote.setShift(updatedShift);
                            newNote.setDate(updatedShift.getShiftDate());
                            noteRepository.save(newNote);
                        }
                    }
            );
        }

        return mapToDTO(updatedShift);
    }

    @Transactional
    public void deleteShift(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));
        shiftRepository.delete(shift);
    }

    private ShiftResponseDTO mapToDTO(Shift shift) {
        ShiftResponseDTO dto = new ShiftResponseDTO();
        dto.setId(shift.getId());
        dto.setShiftDate(shift.getShiftDate());
        dto.setStatus(shift.getStatus());

        if (shift.getPatient() != null) {
            dto.setPatientDni(shift.getPatient().getDni());
            dto.setPatientFullName(shift.getPatient().getFullName());
        }
        if (shift.getDoctor() != null) {
            dto.setDoctorId(shift.getDoctor().getId());
            dto.setDoctorFullName(shift.getDoctor().getName() + " " + shift.getDoctor().getLastname());
        }

        noteRepository.findByShiftId(shift.getId()).ifPresent(note -> {
            dto.setNoteDescription(note.getDescription());
        });

        return dto;
    }
}