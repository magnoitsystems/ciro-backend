package com.ciro.backend.repository;


import com.ciro.backend.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("SELECT m FROM MedicalRecord m " +
            "JOIN Patient p ON m.patient.id = p.id " +
            "WHERE m.patient.dni = :dni")
    boolean existByDNIPatient(String dni);


    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.dni = :dni ORDER BY m.recordDate DESC")
    MedicalRecord getMedicalRecordByPatient(String dni);
}
