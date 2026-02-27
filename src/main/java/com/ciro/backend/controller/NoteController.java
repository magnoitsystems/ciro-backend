package com.ciro.backend.controller;

import com.ciro.backend.dto.NoteDTO;
import com.ciro.backend.entity.Note;
import com.ciro.backend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("api/v1/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping()
    public ResponseEntity<List<NoteDTO>> getNotes() {
        List<NoteDTO> noteDTOS = noteService.getNotes();

        if (noteDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(noteDTOS, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable Long id) {
        NoteDTO noteDTO = noteService.getNoteById(id);

        if (noteDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<List<NoteDTO>> getTaskNotes(@PathVariable Long id) {
        List<NoteDTO> noteDTOS = noteService.getNoteByTask(id);

        if (noteDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(noteDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDTO) {
        Note note = noteService.createNote(noteDTO);

        if (note == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(noteDTO, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO) {
        Note note = noteService.updateNote(id, noteDTO);

        if (note == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<NoteDTO> deleteNote(@PathVariable Long id) {
        NoteDTO noteDTO = noteService.getNoteById(id);
        if (noteDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        noteService.deleteNote(id);
        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }
}
