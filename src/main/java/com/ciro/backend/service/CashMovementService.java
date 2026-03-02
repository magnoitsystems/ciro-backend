package com.ciro.backend.service;

import com.ciro.backend.dto.CashMovementDetailDTO;
import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.enums.*;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.BillRepository;
import com.ciro.backend.repository.CashMovementRepository;
import com.ciro.backend.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CashMovementService {

    @Autowired
    private final CashMovementRepository cashMovementRepository;
    @Autowired
    private final BillRepository billRepository;
    @Autowired
    private final ReceiptRepository receiptRepository;


    public void createMovement(BigDecimal amount, CurrencyType currency, PaymentMethod method,
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

    @Transactional
    public void assignDoctor(Long movementId, Long doctorId) {
        CashMovement movement = cashMovementRepository.findById(movementId)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento de caja no encontrado"));
        movement.setDoctorId(doctorId);
        cashMovementRepository.save(movement);
    }

    public List<CashMovement> getCashMovements(Long doctorId, ReportPeriod period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        LocalDateTime endDate = now.with(LocalTime.MAX);

        if (period == null) {
            startDate = now.minusYears(10);
        } else {
            switch (period) {
                case DAY:
                    startDate = now.with(LocalTime.MIN);
                    break;
                case WEEK:
                    startDate = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).with(LocalTime.MIN);
                    break;
                case MONTH:
                    startDate = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                    break;
                default:
                    startDate = now.minusYears(10);
            }
        }

        return cashMovementRepository.findByFilters(doctorId, startDate, endDate);
    }

    public CashMovementDetailDTO getMovementDetail(Long id) {
        CashMovement movement = cashMovementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        CashMovementDetailDTO dto = new CashMovementDetailDTO();
        dto.setId(movement.getId());
        dto.setAmount(movement.getAmount());
        dto.setCurrencyType(movement.getCurrencyType());
        dto.setPaymentMethod(movement.getPaymentMethod());
        dto.setMovementDate(movement.getMovementDate());
        dto.setType(movement.getType());
        dto.setObservations(movement.getObservations());
        dto.setDoctorId(movement.getDoctorId());

        if (movement.getType() == CashMovementType.INGRESO) {
            dto.setRelatedEntity(receiptRepository.findById(movement.getReferenceId()).orElse(null));
        } else {
            dto.setRelatedEntity(billRepository.findById(movement.getReferenceId()).orElse(null));
        }

        dto.setSuggestedSplits(calculateSplits(movement.getAmount()));

        return dto;
    }

    private List<CashMovementDetailDTO.PercentageSplitDTO> calculateSplits(BigDecimal total) {
        List<CashMovementDetailDTO.PercentageSplitDTO> splits = new ArrayList<>();

        splits.add(createSplit(total, "80/20", 0.80));
        splits.add(createSplit(total, "70/30", 0.70));
        splits.add(createSplit(total, "60/40", 0.60));
        splits.add(createSplit(total, "50/50", 0.50));

        return splits;
    }

    private CashMovementDetailDTO.PercentageSplitDTO createSplit(BigDecimal total, String label, double doctorPercent) {
        CashMovementDetailDTO.PercentageSplitDTO split = new CashMovementDetailDTO.PercentageSplitDTO();
        split.setLabel(label);

        BigDecimal drAmount = total.multiply(BigDecimal.valueOf(doctorPercent)).setScale(2, RoundingMode.HALF_UP);
        split.setDoctorAmount(drAmount);
        split.setClinicAmount(total.subtract(drAmount));

        return split;
    }

    public CashMovementDetailDTO.PercentageSplitDTO calculateCustomSplit(Long movementId, double doctorPercentage) {
        CashMovement movement = cashMovementRepository.findById(movementId)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        return createSplit(movement.getAmount(), (int)(doctorPercentage*100) + "/" + (int)((1-doctorPercentage)*100), doctorPercentage);
    }
}