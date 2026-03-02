package com.ciro.backend.repository;

import com.ciro.backend.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByLabel(String label);
}
