package com.ciro.backend.service;

import com.ciro.backend.dto.TaskCreateDTO;
import com.ciro.backend.dto.TaskResponseDTO;
import com.ciro.backend.entity.Note;
import com.ciro.backend.entity.Task;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.TaskStatus;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.NoteRepository;
import com.ciro.backend.repository.TaskRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La tarea no existe"));
        return mapToDTO(task);
    }

    public List<TaskResponseDTO> findAllByUser(Long userId) {
        return taskRepository.findTaskByUserId(userId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<TaskResponseDTO> findTasksByStatus(TaskStatus status) {
        return taskRepository.findTaskByStatus(status).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public TaskResponseDTO save(TaskCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Task task = new Task();
        task.setUser(user);
        task.setDescription(dto.getDescription());
        task.setTitle(dto.getTitle());
        task.setPriority(dto.getPriority());
        task.setTaskDate(dto.getTaskDate());
        task.setStatus(dto.getStatus());

        Task savedTask = taskRepository.save(task);

        if (dto.getNoteDescription() != null && !dto.getNoteDescription().isEmpty()) {
            Note newNote = new Note();
            newNote.setDescription(dto.getNoteDescription());
            newNote.setTask(savedTask);
            newNote.setDate(savedTask.getTaskDate());
            noteRepository.save(newNote);
        }

        return mapToDTO(savedTask);
    }

    @Transactional
    public TaskResponseDTO update(Long id, TaskCreateDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));

        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getPriority() != null) task.setPriority(dto.getPriority());
        if (dto.getTaskDate() != null) task.setTaskDate(dto.getTaskDate());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            task.setUser(user);
        }

        Task updatedTask = taskRepository.save(task);

        if (dto.getNoteDescription() != null) {
            List<Note> notes = noteRepository.findNoteByTaskId(updatedTask.getId());

            if (!notes.isEmpty()) {
                Note existingNote = notes.get(0);
                existingNote.setDescription(dto.getNoteDescription());
                noteRepository.save(existingNote);
            } else if (!dto.getNoteDescription().isEmpty()) {
                Note newNote = new Note();
                newNote.setDescription(dto.getNoteDescription());
                newNote.setTask(updatedTask);
                newNote.setDate(updatedTask.getTaskDate());
                noteRepository.save(newNote);
            }
        }

        return mapToDTO(updatedTask);
    }

    @Transactional
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
        taskRepository.delete(task);
    }

    public TaskResponseDTO mapToDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setTitle(task.getTitle());
        dto.setTaskDate(task.getTaskDate());

        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
            dto.setUserFullName(task.getUser().getName() + " " + task.getUser().getLastname());
        }

        List<Note> notes = noteRepository.findNoteByTaskId(task.getId());
        if (!notes.isEmpty()) {
            dto.setNoteDescription(notes.get(0).getDescription());
        }

        return dto;
    }
}