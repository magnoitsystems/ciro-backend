package com.ciro.backend.dto;

import com.ciro.backend.entity.User;
import com.ciro.backend.enums.TaskPriority;
import com.ciro.backend.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

public class TaskDTO {
    private User user;
    private LocalDateTime taskDate;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDateTime taskDate) {
        this.taskDate = taskDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}
