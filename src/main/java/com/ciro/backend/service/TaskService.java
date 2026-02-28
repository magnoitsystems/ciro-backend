package com.ciro.backend.service;

import com.ciro.backend.dto.NoteDTO;
import com.ciro.backend.dto.TaskDTO;
import com.ciro.backend.entity.Task;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.TaskStatus;
import com.ciro.backend.repository.TaskRepository;
import com.ciro.backend.repository.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteService noteService;

    //GET ALL
    public List<TaskDTO> findAll() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            taskDTOs.add(mapToDTO(task));
        }
        return taskDTOs;
    }

    //GET BY ID
    public TaskDTO findById(Long id) {
        if(id >= 0){
            Task task = taskRepository.findById(id).orElseThrow( () -> new RuntimeException("Tarea no encontrada"));
            return mapToDTO(task);
        }
        return null;
    }

    //GET BY USER
    public List<TaskDTO> findAllByUser(Long userId) {
        if(userId >= 0){
            List<Task> tasksByUser = taskRepository.findTaskByUserId(userId);
            List<TaskDTO> taskDTOs = new ArrayList<>();
            for (Task task : tasksByUser) {
                taskDTOs.add(mapToDTO(task));
            }
            return taskDTOs;
        }
        return null;
    }

    //GET BY STATUS
    public List<TaskDTO> findTasksByStatus(TaskStatus status) {
        return taskRepository.findTaskByStatus(status)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    //POST
    public Task save(TaskDTO taskDTO, @Nullable NoteDTO noteDTO) {
        User user = userRepository.findById(taskDTO.getUser().getId()).orElseThrow( () -> new RuntimeException("Usuario no encontrado"));
        Task task = new Task();
        task.setUser(user);
        task.setDescription(taskDTO.getDescription());
        task.setTitle(taskDTO.getTitle());
        task.setPriority(taskDTO.getPriority());
        task.setTaskDate(taskDTO.getTaskDate());
        task.setStatus(taskDTO.getStatus());

        Task savedTask = taskRepository.save(task);

        if(noteDTO != null){
            if(noteDTO.getDescription() != null && noteDTO.getShift() != null) {
                noteDTO.setTask(task);
                noteService.createNote(noteDTO);
            }
        }

        return savedTask;
    }

    //PUT
    public Task update(TaskDTO taskDTO, Long id) {
        if(id >= 0){

            Task task = taskRepository.findById(id).orElseThrow( () -> new RuntimeException("Task no encontrado"));
            if(taskDTO.getDescription() != null){
                task.setDescription(taskDTO.getDescription());
            }
            if(taskDTO.getTitle() != null){
                task.setTitle(taskDTO.getTitle());
            }
            if(taskDTO.getPriority() != null){
                task.setPriority(taskDTO.getPriority());
            }
            if(taskDTO.getTaskDate() != null){
                task.setTaskDate(taskDTO.getTaskDate());
            }
            if(taskDTO.getStatus() != null){
                task.setStatus(taskDTO.getStatus());
            }
            if(taskDTO.getUser() != null) {
                User user = userRepository.findById(taskDTO.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                task.setUser(user);
            }

            return taskRepository.save(task);
        }
        return null;
    }

    //DELETE
    public void delete(Long id) {
        if(id >= 0){
            taskRepository.deleteById(id);
        }
    }


    public TaskDTO mapToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setTitle(task.getTitle());
        dto.setUser(task.getUser());
        dto.setTaskDate(task.getTaskDate());

        return dto;
    }
}
