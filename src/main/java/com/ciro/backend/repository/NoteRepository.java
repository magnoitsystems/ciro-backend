package com.ciro.backend.repository;

import com.ciro.backend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.task.id = :idTask")
    List<Note> findNoteByTaskId(Long idTask);
}
