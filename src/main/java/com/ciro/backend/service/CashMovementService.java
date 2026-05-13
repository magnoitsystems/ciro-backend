package com.ciro.backend.service;

import com.ciro.backend.dto.CashBalanceDTO;
import com.ciro.backend.dto.CashMovementDetailDTO;
import com.ciro.backend.dto.RevenueWidgetDTO;
import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.*;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.BillRepository;
import com.ciro.backend.repository.CashMovementRepository;
import com.ciro.backend.repository.ReceiptRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class CashMovementService {

    @Autowired
    private CashMovementRepository cashMovementRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private UserRepository userRepository;

    public void createMovement(BigDecimal amount, CurrencyType currency, PaymentMethod method,
                               Long referenceId, CashMovementType type, String observations, Long doctorId) {
        CashMovement movement = new CashMovement();
        movement.setAmount(amount);
        movement.setCurrencyType(currency);
        movement.setPaymentMethod(method);
        movement.setReferenceId(referenceId);
        movement.setType(type);
        movement.setMovementDate(LocalDateTime.now());
        movement.setObservations(observations);
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

    public CashBalanceDTO getCashBalance(ReportPeriod period) {
        List<CashMovement> movements = getCashMovements(null, period);

        BigDecimal ingresosPesos = BigDecimal.ZERO;
        BigDecimal egresosPesos = BigDecimal.ZERO;
        BigDecimal ingresosDolares = BigDecimal.ZERO;
        BigDecimal egresosDolares = BigDecimal.ZERO;

        for (CashMovement movement : movements) {
            if (movement.getCurrencyType() == CurrencyType.PESOS) {
                if (movement.getType() == CashMovementType.INGRESO) {
                    ingresosPesos = ingresosPesos.add(movement.getAmount());
                } else if (movement.getType() == CashMovementType.EGRESO) {
                    egresosPesos = egresosPesos.add(movement.getAmount());
                }
            } else if (movement.getCurrencyType() == CurrencyType.DOLARES) {
                if (movement.getType() == CashMovementType.INGRESO) {
                    ingresosDolares = ingresosDolares.add(movement.getAmount());
                } else if (movement.getType() == CashMovementType.EGRESO) {
                    egresosDolares = egresosDolares.add(movement.getAmount());
                }
            }
        }

        BigDecimal balancePesos = ingresosPesos.subtract(egresosPesos);
        BigDecimal balanceDolares = ingresosDolares.subtract(egresosDolares);

        return new CashBalanceDTO(
                balancePesos, balanceDolares,
                ingresosPesos, egresosPesos,
                ingresosDolares, egresosDolares
        );
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

            if (movement.getDoctorId() != null) {
                dto.setSuggestedSplits(calculateSplits(movement.getAmount()));
            }
        } else {
            dto.setRelatedEntity(billRepository.findById(movement.getReferenceId()).orElse(null));
        }

        return dto;
    }

    private List<CashMovementDetailDTO.PercentageSplitDTO> calculateSplits(BigDecimal total) {
        List<CashMovementDetailDTO.PercentageSplitDTO> splits = new ArrayList<>();

        splits.add(createSplit(total, "20% Doctor / 80% CIRO", 0.20));
        splits.add(createSplit(total, "30% Doctor / 70% CIRO", 0.30));
        splits.add(createSplit(total, "40% Doctor / 60% CIRO", 0.40));
        splits.add(createSplit(total, "50% Doctor / 50% CIRO", 0.50));

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

        int docLabel = (int)(doctorPercentage * 100);
        int ciroLabel = 100 - docLabel;

        return createSplit(movement.getAmount(), docLabel + "% Doctor / " + ciroLabel + "% CIRO", doctorPercentage);
    }

    public List<CashMovement> getCashMovementsByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + id + " no existe"));

        return cashMovementRepository.findByDoctorIdAndType(id, CashMovementType.INGRESO);
    }

    public RevenueWidgetDTO getNetRevenueWidget(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            LocalDate today = LocalDate.now();
            startDate = today.with(java.time.DayOfWeek.MONDAY);
            endDate = today.with(java.time.DayOfWeek.SUNDAY);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<CashMovement> movements = cashMovementRepository.findByFilters(null, startDateTime, endDateTime);

        BigDecimal balancePesos = BigDecimal.ZERO;
        BigDecimal balanceDolares = BigDecimal.ZERO;

        for (CashMovement movement : movements) {
            if (movement.getCurrencyType() == CurrencyType.PESOS) {
                if (movement.getType() == CashMovementType.INGRESO) {
                    balancePesos = balancePesos.add(movement.getAmount());
                } else if (movement.getType() == CashMovementType.EGRESO) {
                    balancePesos = balancePesos.subtract(movement.getAmount());
                }
            } else if (movement.getCurrencyType() == CurrencyType.DOLARES) {
                if (movement.getType() == CashMovementType.INGRESO) {
                    balanceDolares = balanceDolares.add(movement.getAmount());
                } else if (movement.getType() == CashMovementType.EGRESO) {
                    balanceDolares = balanceDolares.subtract(movement.getAmount());
                }
            }
        }

        return new RevenueWidgetDTO(balancePesos, balanceDolares);
    }

    @Transactional
    public void deleteMovementByReference(Long referenceId, CashMovementType type) {
        List<CashMovement> movements = cashMovementRepository.findByReferenceIdAndType(referenceId, type);
        if (!movements.isEmpty()) {
            cashMovementRepository.deleteAll(movements);
        }
    }
}