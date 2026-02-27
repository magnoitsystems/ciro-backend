package com.ciro.backend.controller;

import com.ciro.backend.dto.TaskDTO;
import com.ciro.backend.entity.Task;
import com.ciro.backend.enums.TaskStatus;
import com.ciro.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getTasks() {
        List<TaskDTO> taskDTOS = taskService.findAll();

        if(taskDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskDTO taskDTOS = taskService.findById(id);

        if(taskDTOS == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TaskDTO>> getTaskByUserId(@PathVariable Long id) {
        List<TaskDTO> taskDTOS = taskService.findAllByUser(id);
        if(taskDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskDTO>> getTaskByStatus(@PathVariable TaskStatus status) {

        List<TaskDTO> taskDTOS = taskService.findTasksByStatus(status);

        if(taskDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        Task newTask = taskService.save(taskDTO);
        if(newTask == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(taskDTO, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        Task newTask = taskService.update(taskDTO, id);
        if(newTask == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.findById(id);
        if(taskDTO == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        taskService.delete(id);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }
}
