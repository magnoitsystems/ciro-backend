package com.ciro.backend.repository;

import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.CashMovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CashMovementRepository extends JpaRepository<CashMovement, Long> {

    @Query("SELECT c FROM CashMovement c WHERE " +
            "(:doctorId IS NULL OR c.doctorId = :doctorId) AND " +
            "(c.movementDate BETWEEN :startDate AND :endDate) " +
            "ORDER BY c.movementDate DESC")
    List<CashMovement> findByFilters(@Param("doctorId") Long doctorId,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    boolean existsByTypeAndReferenceId(CashMovementType type, Long referenceId);
}
