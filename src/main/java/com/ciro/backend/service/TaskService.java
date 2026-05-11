package com.ciro.backend.service;

import com.ciro.backend.dto.SubtaskDTO;
import com.ciro.backend.dto.TaskCreateDTO;
import com.ciro.backend.dto.TaskResponseDTO;
import com.ciro.backend.dto.TaskWidgetDTO;
import com.ciro.backend.entity.Note;
import com.ciro.backend.entity.Subtask;
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

import java.util.ArrayList;
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
        task.setEvaluation(dto.getEvaluation());

        if (dto.getSubtasks() != null && !dto.getSubtasks().isEmpty()) {
            for (SubtaskDTO subDto : dto.getSubtasks()) {
                Subtask subtask = new Subtask();
                subtask.setTitle(subDto.getTitle());
                subtask.setDescription(subDto.getDescription());
                subtask.setStatus(subDto.getStatus() != null ? subDto.getStatus() : TaskStatus.PENDING);
                subtask.setEvaluation(subDto.getEvaluation());
                subtask.setTask(task);
                task.getSubtasks().add(subtask);
            }
        }

        enforceCompletionRules(task);

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
        if (dto.getEvaluation() != null) task.setEvaluation(dto.getEvaluation());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            task.setUser(user);
        }

        if (dto.getSubtasks() != null) {
            task.getSubtasks().removeIf(existingSub ->
                    dto.getSubtasks().stream().noneMatch(s -> s.getId() != null && s.getId().equals(existingSub.getId()))
            );

            for (SubtaskDTO subDto : dto.getSubtasks()) {
                if (subDto.getId() != null) {
                    Subtask existing = task.getSubtasks().stream().filter(s -> s.getId().equals(subDto.getId())).findFirst().orElse(null);
                    if (existing != null) {
                        existing.setTitle(subDto.getTitle());
                        existing.setDescription(subDto.getDescription());
                        existing.setStatus(subDto.getStatus());
                        existing.setEvaluation(subDto.getEvaluation());
                    }
                } else {
                    Subtask newSub = new Subtask();
                    newSub.setTitle(subDto.getTitle());
                    newSub.setDescription(subDto.getDescription());
                    newSub.setStatus(subDto.getStatus() != null ? subDto.getStatus() : TaskStatus.PENDING);
                    newSub.setEvaluation(subDto.getEvaluation());
                    newSub.setTask(task);
                    task.getSubtasks().add(newSub);
                }
            }
        }

        enforceCompletionRules(task);

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

    private void enforceCompletionRules(Task task) {
        if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
            boolean allCompleted = task.getSubtasks().stream()
                    .allMatch(s -> s.getStatus() == TaskStatus.COMPLETED);

            if (allCompleted) {
                task.setStatus(TaskStatus.COMPLETED);
            } else if (task.getStatus() == TaskStatus.COMPLETED) {
                task.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public TaskResponseDTO mapToDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setTitle(task.getTitle());
        dto.setTaskDate(task.getTaskDate());
        dto.setEvaluation(task.getEvaluation());

        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
            dto.setUserFullName(task.getUser().getName() + " " + task.getUser().getLastname());
        }

        List<Note> notes = noteRepository.findNoteByTaskId(task.getId());
        if (!notes.isEmpty()) {
            dto.setNoteDescription(notes.get(0).getDescription());
        }

        if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
            List<SubtaskDTO> subtaskDTOs = new ArrayList<>();
            for (Subtask subtask : task.getSubtasks()) {
                SubtaskDTO subDto = new SubtaskDTO();
                subDto.setId(subtask.getId());
                subDto.setTitle(subtask.getTitle());
                subDto.setDescription(subtask.getDescription());
                subDto.setStatus(subtask.getStatus());
                subDto.setEvaluation(subtask.getEvaluation());
                subtaskDTOs.add(subDto);
            }
            dto.setSubtasks(subtaskDTOs);
        }

        return dto;
    }

    public TaskWidgetDTO getPendingTasksWidget() {
        List<TaskResponseDTO> pendingTasks = findTasksByStatus(TaskStatus.PENDING);
        return new TaskWidgetDTO(pendingTasks.size(), pendingTasks);
    }
}