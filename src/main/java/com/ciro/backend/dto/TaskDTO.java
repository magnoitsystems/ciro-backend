package com.ciro.backend.dto;

import com.ciro.backend.entity.User;
import com.ciro.backend.enums.TaskPriority;
import com.ciro.backend.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private User user;
    private LocalDateTime taskDate;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
}
