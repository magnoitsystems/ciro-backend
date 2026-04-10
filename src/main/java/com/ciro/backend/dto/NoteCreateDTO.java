package com.ciro.backend.dto;

import java.time.LocalDateTime;

public class NoteCreateDTO {
    private String description;
    private LocalDateTime date;
    private Long shiftId;
    private Long taskId;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Long getShiftId() { return shiftId; }
    public void setShiftId(Long shiftId) { this.shiftId = shiftId; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
}