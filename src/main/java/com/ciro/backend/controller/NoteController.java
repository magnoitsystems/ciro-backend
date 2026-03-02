package com.ciro.backend.controller;

import com.ciro.backend.dto.NoteDTO;
import com.ciro.backend.entity.Note;
import com.ciro.backend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping()
    public ResponseEntity<List<NoteDTO>> getNotes() {
        List<NoteDTO> noteDTOS = noteService.getNotes();

        return new ResponseEntity<>(noteDTOS, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable Long id) {
        NoteDTO noteDTO = noteService.getNoteById(id);

        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<List<NoteDTO>> getTaskNotes(@PathVariable Long id) {
        List<NoteDTO> noteDTOS = noteService.getNoteByTask(id);

        return new ResponseEntity<>(noteDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody NoteDTO noteDTO) {
        Note note = noteService.createNote(noteDTO);

        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO) {
        Note note = noteService.updateNote(id, noteDTO);

        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<NoteDTO> deleteNote(@PathVariable Long id) {
        NoteDTO noteDTO = noteService.getNoteById(id);

        noteService.deleteNote(id);
        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }
}
