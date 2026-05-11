package com.ciro.backend.dto;

import com.ciro.backend.enums.TaskStatus;

public class SubtaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private String evaluation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public String getEvaluation() { return evaluation; }
    public void setEvaluation(String evaluation) { this.evaluation = evaluation; }
}