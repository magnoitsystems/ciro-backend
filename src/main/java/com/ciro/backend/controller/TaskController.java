package com.ciro.backend.controller;

import com.ciro.backend.dto.TaskCreateDTO;
import com.ciro.backend.dto.TaskResponseDTO;
import com.ciro.backend.enums.TaskStatus;
import com.ciro.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TaskResponseDTO>> getTaskByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findAllByUser(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDTO>> getTaskByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.findTasksByStatus(status));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO taskDTO) {
        return new ResponseEntity<>(taskService.save(taskDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskCreateDTO taskDTO) {
        return ResponseEntity.ok(taskService.update(id, taskDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}