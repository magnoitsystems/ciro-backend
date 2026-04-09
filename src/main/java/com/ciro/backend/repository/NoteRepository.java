package com.ciro.backend.repository;

import com.ciro.backend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.task.id = :idTask")
    List<Note> findNoteByTaskId(Long idTask);

    Optional<Note> findByShiftId(Long shiftId);

    List<Note> findByTaskIsNullAndShiftIsNullAndDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
