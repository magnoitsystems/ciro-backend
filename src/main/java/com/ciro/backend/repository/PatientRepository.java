package com.ciro.backend.repository;

import com.ciro.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByDni(String dni);

    @Query("SELECT p FROM Patient p WHERE " +
            "(:dni IS NULL OR p.dni LIKE CONCAT('%', :dni, '%')) AND " +
            "(:fullName IS NULL OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND " +
            "(:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')))")

    List<Patient> findByFilters(
            @Param("dni") String dni,
            @Param("fullName") String fullName,
            @Param("city") String city
    );
}