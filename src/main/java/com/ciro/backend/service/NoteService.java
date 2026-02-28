package com.ciro.backend.service;

import com.ciro.backend.dto.NoteDTO;
import com.ciro.backend.entity.Note;
import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.Task;
import com.ciro.backend.repository.NoteRepository;
import com.ciro.backend.repository.ShiftRepository;
import com.ciro.backend.repository.TaskRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    public List<NoteDTO> getNotes() {
        List<Note> notes = noteRepository.findAll();
        List<NoteDTO> noteDTOs = new ArrayList<>();

        for (Note note : notes) {
            noteDTOs.add(matToDTO(note));
        }

        return noteDTOs;
    }

    public NoteDTO getNoteById(Long noteId) {
        if(noteId >= 0){
            Note note = noteRepository.findById(noteId).orElseThrow(()->new RuntimeException("Note no encontrado"));
            return matToDTO(note);
        }
        return null;
    }

    public Note createNote(NoteDTO noteDTO) {
        Note note = new Note();

        Task task = taskRepository.findById(noteDTO.getTask().getId()).orElseThrow(()->new RuntimeException("Task no encontrado"));

        assert task != null;

        note.setDate(noteDTO.getDate());
        note.setDescription(noteDTO.getDescription());
        note.setTask(noteDTO.getTask());
        note.setShift(noteDTO.getShift());

        return noteRepository.save(note);
    }

    public Note updateNote(Long noteId, NoteDTO noteDTO) {
        if(noteId >= 0){
            Note note = noteRepository.findById(noteId).orElseThrow(()->new RuntimeException("Note no encontrado"));
            if(noteDTO.getTask() != null){
                Task task = taskRepository.findById(noteDTO.getTask().getId()).orElseThrow(()->new RuntimeException("Task no encontrado"));
                note.setTask(task);
            }

            if(noteDTO.getShift() != null){
                Shift shift = shiftRepository.findById(noteDTO.getShift().getId()).orElseThrow(()->new RuntimeException("Shift no encontrado"));
                note.setShift(shift);
            }

            if(noteDTO.getDescription() != null){
                note.setDescription(noteDTO.getDescription());
            }

            if(noteDTO.getDate() != null){
                note.setDate(noteDTO.getDate());
            }

            return noteRepository.save(note);
        }
        return null;
    }

    public void deleteNote(Long noteId) {
        if(noteId >= 0){
            noteRepository.deleteById(noteId);
        }
    }

    public List<NoteDTO> getNoteByTask(Long taskId) {
        if(taskId >= 0){
            List<Note> note = noteRepository.findNoteByTaskId(taskId);
            List<NoteDTO> noteDTOS = new ArrayList<>();

            for (Note note1 : note) {
                noteDTOS.add(matToDTO(note1));
            }

            return noteDTOS;
        }
        return null;
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
