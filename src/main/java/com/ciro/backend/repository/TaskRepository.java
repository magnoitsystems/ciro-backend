package com.ciro.backend.repository;

import com.ciro.backend.entity.Task;
import com.ciro.backend.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.user.id = :id")
    List<Task> findTaskByUserId(Long id);

    @Query("SELECT t FROM Task t WHERE t.status = :mode")
    List<Task> findTaskByStatus(TaskStatus mode);
}
