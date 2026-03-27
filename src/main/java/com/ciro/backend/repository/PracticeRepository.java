package com.ciro.backend.repository;

import com.ciro.backend.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {

    @Query("SELECT p FROM Practice p WHERE LOWER(p.practiceType) ILIKE LOWER(:type) ")
    List<Practice> getPracticesByType(String type);
}
