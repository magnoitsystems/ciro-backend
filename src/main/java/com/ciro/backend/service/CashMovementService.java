package com.ciro.backend.service;

import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.*;
import com.ciro.backend.repository.CashMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CashMovementService {

    private final CashMovementRepository cashMovementRepository;

    public void registrarMovimiento(BigDecimal amount, CurrencyType currency, PaymentMethod method,
                                    Long referenceId, CashMovementType type, String observations) {
        CashMovement movement = new CashMovement();
        movement.setAmount(amount);
        movement.setCurrencyType(currency);
        movement.setPaymentMethod(method);
        movement.setReferenceId(referenceId);
        movement.setType(type);
        movement.setMovementDate(LocalDateTime.now());
        movement.setObservations(observations);

        cashMovementRepository.save(movement);
    }
}