package com.ciro.backend.dto;

import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.Task;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDTO {
    private String description;
    private LocalDateTime date;
    private Shift shift;
    private Task task;
}
