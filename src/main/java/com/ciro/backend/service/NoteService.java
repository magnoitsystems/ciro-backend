package com.ciro.backend.service;

import com.ciro.backend.dto.NoteDTO;
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

    public List<NoteDTO> getNotes() {
        return noteRepository.findAll().stream().map(this::matToDTO).collect(Collectors.toList());
    }

    public NoteDTO getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Nota " + noteId + " no encontrada"));
        return matToDTO(note);
    }

    public List<NoteDTO> getIndependentNotesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<Note> notes = noteRepository.findByTaskIsNullAndShiftIsNullAndDateBetween(startOfDay, endOfDay);
        return notes.stream().map(this::matToDTO).collect(Collectors.toList());
    }

    public List<NoteDTO> getNoteByTask(Long taskId) {
        List<Note> notes = noteRepository.findNoteByTaskId(taskId);
        return notes.stream().map(this::matToDTO).collect(Collectors.toList());
    }

    public NoteDTO createNote(NoteDTO noteDTO) {
        Note note = new Note();
        note.setDate(noteDTO.getDate() != null ? noteDTO.getDate() : LocalDateTime.now());
        note.setDescription(noteDTO.getDescription());

        if (noteDTO.getTask() != null && noteDTO.getTask().getId() != null) {
            Task task = taskRepository.findById(noteDTO.getTask().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
            note.setTask(task);
        }

        if (noteDTO.getShift() != null && noteDTO.getShift().getId() != null) {
            Shift shift = shiftRepository.findById(noteDTO.getShift().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado"));
            note.setShift(shift);
        }

        Note savedNote = noteRepository.save(note);
        return matToDTO(savedNote);
    }

    public NoteDTO updateNote(Long noteId, NoteDTO noteDTO) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Nota " + noteId + " no encontrada"));

        if (noteDTO.getTask() != null && noteDTO.getTask().getId() != null) {
            Task task = taskRepository.findById(noteDTO.getTask().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
            note.setTask(task);
        }

        if (noteDTO.getShift() != null && noteDTO.getShift().getId() != null) {
            Shift shift = shiftRepository.findById(noteDTO.getShift().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado"));
            note.setShift(shift);
        }

        if (noteDTO.getDescription() != null) note.setDescription(noteDTO.getDescription());
        if (noteDTO.getDate() != null) note.setDate(noteDTO.getDate());

        Note updatedNote = noteRepository.save(note);
        return matToDTO(updatedNote);
    }

    public void deleteNote(Long noteId) {
        if (noteRepository.existsById(noteId)) {
            noteRepository.deleteById(noteId);
        } else {
            throw new ResourceNotFoundException("Nota " + noteId + " no encontrada");
        }
    }

    public NoteDTO matToDTO(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setDate(note.getDate());
        noteDTO.setDescription(note.getDescription());
        noteDTO.setTask(note.getTask());
        noteDTO.setShift(note.getShift());
        return noteDTO;
    }
}