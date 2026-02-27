package com.ciro.backend.repository;

import com.ciro.backend.entity.Budget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.patient.id = :id")
    List<Budget> findByPatientId(Long id);
}
