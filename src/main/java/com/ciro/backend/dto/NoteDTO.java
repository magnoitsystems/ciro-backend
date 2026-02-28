package com.ciro.backend.dto;

import com.ciro.backend.entity.Shift;
import com.ciro.backend.entity.Task;
import lombok.Data;

import java.time.LocalDateTime;

public class NoteDTO {
    private String description;
    private LocalDateTime date;
    private Shift shift;
    private Task task;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
