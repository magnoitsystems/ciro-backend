package com.ciro.backend.repository;

import com.ciro.backend.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    @Query("SELECT s FROM Shift s WHERE s.patient.dni = :dni")
    List<Shift> getByPatient(String dni);

    @Query("SELECT s FROM Shift s WHERE s.doctor.id = :id")
    List<Shift> getByDoctor(Long id);
}
