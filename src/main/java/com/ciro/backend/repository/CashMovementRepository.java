package com.ciro.backend.repository;

import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.CashMovementType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashMovementRepository extends JpaRepository<CashMovement, Long> {

    boolean existsByTypeAndReferenceId(CashMovementType type, Long referenceId);
}
