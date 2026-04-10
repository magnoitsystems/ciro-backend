package com.ciro.backend.controller;

import com.ciro.backend.dto.NoteCreateDTO;
import com.ciro.backend.dto.NoteResponseDTO;
import com.ciro.backend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteResponseDTO>> getNotes() {
        return ResponseEntity.ok(noteService.getNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> getNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<List<NoteResponseDTO>> getTaskNotes(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteByTask(id));
    }

    @GetMapping("/independent/{date}")
    public ResponseEntity<List<NoteResponseDTO>> getIndependentNotesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(noteService.getIndependentNotesByDate(date));
    }

    @PostMapping
    public ResponseEntity<NoteResponseDTO> createNote(@RequestBody NoteCreateDTO noteDTO) {
        NoteResponseDTO createdNote = noteService.createNote(noteDTO);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> updateNote(@PathVariable Long id, @RequestBody NoteCreateDTO noteDTO) {
        NoteResponseDTO updatedNote = noteService.updateNote(id, noteDTO);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}