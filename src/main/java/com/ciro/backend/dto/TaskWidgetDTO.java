package com.ciro.backend.dto;

import java.util.List;

public class TaskWidgetDTO {
    private int pendingCount;
    private List<TaskResponseDTO> pendingTasks;

    public TaskWidgetDTO(int pendingCount, List<TaskResponseDTO> pendingTasks) {
        this.pendingCount = pendingCount;
        this.pendingTasks = pendingTasks;
    }

    public int getPendingCount() { return pendingCount; }
    public void setPendingCount(int pendingCount) { this.pendingCount = pendingCount; }
    public List<TaskResponseDTO> getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(List<TaskResponseDTO> pendingTasks) { this.pendingTasks = pendingTasks; }
}