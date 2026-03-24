package com.ciro.backend.repository;

import com.ciro.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    boolean existsByDni(String dni);

    @Query("SELECT p FROM Patient p WHERE " +
            "(:dni = '' OR p.dni LIKE CONCAT('%', :dni, '%')) AND " +
            "(:fullName = '' OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND " +
            "(:city = '' OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')))")
    List<Patient> findByFilters(
            @Param("dni") String dni,
            @Param("fullName") String fullName,
            @Param("city") String city
    );

    @Query("SELECT p FROM Patient p WHERE p.dni = :dni")
    Patient findByDni(@Param("dni") String dni);

    @Query("SELECT COUNT(p) FROM Patient p")
    long countTotalPatients();

    @Query("SELECT p.from, COUNT(p) FROM Patient p WHERE p.from IS NOT NULL GROUP BY p.from")
    List<Object[]> countPatientsByOrigin();

    @Query("SELECT p.city, COUNT(p) FROM Patient p WHERE p.city IS NOT NULL AND p.city != '' GROUP BY p.city")
    List<Object[]> countPatientsByCity();
}