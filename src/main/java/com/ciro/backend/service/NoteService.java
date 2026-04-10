package com.ciro.backend.service;

import com.ciro.backend.dto.NoteCreateDTO;
import com.ciro.backend.dto.NoteResponseDTO;
import com.ciro.backend.entity.Note;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.Task;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.NoteRepository;
import com.ciro.backend.repository.ShiftRepository;
import com.ciro.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    public List<NoteResponseDTO> getNotes() {
        return noteRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public NoteResponseDTO getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Nota " + noteId + " no encontrada"));
        return mapToDTO(note);
    }

    public List<NoteResponseDTO> getIndependentNotesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<Note> notes = noteRepository.findByTaskIsNullAndShiftIsNullAndDateBetween(startOfDay, endOfDay);
        return notes.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<NoteResponseDTO> getNoteByTask(Long taskId) {
        List<Note> notes = noteRepository.findNoteByTaskId(taskId);
        return notes.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public NoteResponseDTO createNote(NoteCreateDTO dto) {
        Note note = new Note();
        note.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
        note.setDescription(dto.getDescription());

        if (dto.getTaskId() != null) {
            Task task = taskRepository.findById(dto.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
            note.setTask(task);
        }

        if (dto.getShiftId() != null) {
            Shift shift = shiftRepository.findById(dto.getShiftId())
                    .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado"));
            note.setShift(shift);
        }

        Note savedNote = noteRepository.save(note);
        return mapToDTO(savedNote);
    }

    public NoteResponseDTO updateNote(Long noteId, NoteCreateDTO dto) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Nota " + noteId + " no encontrada"));

        if (dto.getTaskId() != null) {
            Task task = taskRepository.findById(dto.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
            note.setTask(task);
        }

        if (dto.getShiftId() != null) {
            Shift shift = shiftRepository.findById(dto.getShiftId())
                    .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado"));
            note.setShift(shift);
        }

        if (dto.getDescription() != null) note.setDescription(dto.getDescription());
        if (dto.getDate() != null) note.setDate(dto.getDate());

        Note updatedNote = noteRepository.save(note);
        return mapToDTO(updatedNote);
    }

    public void deleteNote(Long noteId) {
        if (noteRepository.existsById(noteId)) {
            noteRepository.deleteById(noteId);
        } else {
            throw new ResourceNotFoundException("Nota " + noteId + " no encontrada");
        }
    }

    public NoteResponseDTO mapToDTO(Note note) {
        NoteResponseDTO dto = new NoteResponseDTO();
        dto.setId(note.getId());
        dto.setDescription(note.getDescription());
        dto.setDate(note.getDate());

        if (note.getTask() != null) dto.setTaskId(note.getTask().getId());
        if (note.getShift() != null) dto.setShiftId(note.getShift().getId());

        return dto;
    }
}