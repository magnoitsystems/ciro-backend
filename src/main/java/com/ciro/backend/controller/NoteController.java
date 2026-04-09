package com.ciro.backend.controller;

import com.ciro.backend.dto.NoteDTO;
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
    public ResponseEntity<List<NoteDTO>> getNotes() {
        return ResponseEntity.ok(noteService.getNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<List<NoteDTO>> getTaskNotes(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteByTask(id));
    }

    @GetMapping("/independent/{date}")
    public ResponseEntity<List<NoteDTO>> getIndependentNotesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<NoteDTO> notes = noteService.getIndependentNotesByDate(date);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDTO) {
        NoteDTO createdNote = noteService.createNote(noteDTO);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO) {
        NoteDTO updatedNote = noteService.updateNote(id, noteDTO);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}