package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.CashMovement;
import com.ciro.backend.entity.Patient;
import com.ciro.backend.enums.CashMovementType;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.repository.BillRepository;
import com.ciro.backend.repository.CashMovementRepository;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired private ReceiptRepository receiptRepository;
    @Autowired private BillRepository billRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private PatientService patientService;
    @Autowired private CashMovementRepository cashMovementRepository;

    public StatisticsResponseDTO getDashboardStatistics(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            LocalDate today = LocalDate.now();
            startDate = today.withDayOfMonth(1);
            endDate = today.withDayOfMonth(today.lengthOfMonth());
        }

        StatisticsResponseDTO response = new StatisticsResponseDTO();
        response.setFinancial(buildFinancialStats(startDate, endDate));
        response.setPatients(buildPatientStats(startDate, endDate));

        return response;
    }

    private FinancialStatsDTO buildFinancialStats(LocalDate startDate, LocalDate endDate) {
        FinancialStatsDTO finStats = new FinancialStatsDTO();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<CashMovement> movements = cashMovementRepository.findByFilters(null, startDateTime, endDateTime);

        BigDecimal incPesos = BigDecimal.ZERO, incDolares = BigDecimal.ZERO;
        BigDecimal expPesos = BigDecimal.ZERO, expDolares = BigDecimal.ZERO;

        for (CashMovement m : movements) {
            if (m.getType() == CashMovementType.INGRESO) {
                if (m.getCurrencyType() == CurrencyType.PESOS) incPesos = incPesos.add(m.getAmount());
                else incDolares = incDolares.add(m.getAmount());
            } else {
                if (m.getCurrencyType() == CurrencyType.PESOS) expPesos = expPesos.add(m.getAmount());
                else expDolares = expDolares.add(m.getAmount());
            }
        }

        finStats.setCurrentPeriodIncomePesos(incPesos);
        finStats.setCurrentPeriodIncomeDollars(incDolares);
        finStats.setCurrentPeriodExpensesPesos(expPesos);
        finStats.setCurrentPeriodExpensesDollars(expDolares);

        finStats.setNetProfitPesos(incPesos.subtract(expPesos));
        finStats.setNetProfitDollars(incDolares.subtract(expDolares));

        finStats.setIncomeBreakdown(buildMovementBreakdown(movements, CashMovementType.INGRESO));
        finStats.setExpensesBreakdown(buildMovementBreakdown(movements, CashMovementType.EGRESO));

        return finStats;
    }

    private List<StatItemDTO> buildMovementBreakdown(List<CashMovement> movements, CashMovementType type) {
        List<CashMovement> filtered = movements.stream().filter(m -> m.getType() == type).toList();

        Map<String, List<CashMovement>> grouped = filtered.stream()
                .collect(Collectors.groupingBy(m -> m.getCurrencyType().name() + " - " + m.getPaymentMethod().name()));

        List<StatItemDTO> list = new ArrayList<>();

        BigDecimal totalPesos = filtered.stream().filter(m -> m.getCurrencyType() == CurrencyType.PESOS).map(CashMovement::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDolares = filtered.stream().filter(m -> m.getCurrencyType() == CurrencyType.DOLARES).map(CashMovement::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        for (Map.Entry<String, List<CashMovement>> entry : grouped.entrySet()) {
            String label = entry.getKey();
            List<CashMovement> movs = entry.getValue();

            BigDecimal amount = movs.stream().map(CashMovement::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            List<DrillDownDetailDTO> details = movs.stream().map(m -> new DrillDownDetailDTO(
                    m.getId(),
                    m.getObservations() != null && !m.getObservations().isEmpty() ? m.getObservations() : "Movimiento sin descripción",
                    "Monto: $" + m.getAmount() + " (" + m.getMovementDate().toLocalDate() + ")"
            )).toList();

            BigDecimal totalRef = label.startsWith("PESOS") ? totalPesos : totalDolares;
            double percentage = 0.0;
            if (totalRef.compareTo(BigDecimal.ZERO) > 0) {
                percentage = amount.divide(totalRef, 4, RoundingMode.HALF_UP).doubleValue() * 100;
            }

            list.add(new StatItemDTO(label, amount, Math.round(percentage * 10.0) / 10.0, details));
        }
        return list;
    }

    private PatientStatsDTO buildPatientStats(LocalDate startDate, LocalDate endDate) {
        PatientStatsDTO patStats = new PatientStatsDTO();
        List<Patient> allPatients = patientRepository.findAll();
        long totalPatients = allPatients.size();

        List<PatientDebtorDTO> debtors = patientService.getDebtorPatients();
        long totalDebtors = debtors.size();
        long totalNonDebtors = totalPatients - totalDebtors;

        patStats.setTotalPatients(totalPatients);
        patStats.setTotalDebtors(totalDebtors);
        patStats.setTotalNonDebtors(totalNonDebtors);

        List<DrillDownDetailDTO> debtorsDetails = debtors.stream().map(d -> new DrillDownDetailDTO(
                d.getId(),
                d.getFullName(),
                "DNI: " + d.getDni() + " | Deuda: $" + d.getDebtPesos() + " / U$D " + d.getDebtDolares()
        )).toList();
        patStats.setDebtorsDetails(debtorsDetails);

        List<Long> debtorIds = debtors.stream().map(PatientDebtorDTO::getId).toList();
        List<DrillDownDetailDTO> nonDebtorsDetails = allPatients.stream()
                .filter(p -> !debtorIds.contains(p.getId()))
                .map(p -> new DrillDownDetailDTO(p.getId(), p.getFullName(), "DNI: " + p.getDni() + " (Al día)"))
                .toList();
        patStats.setNonDebtorsDetails(nonDebtorsDetails);

        patStats.setPatientsByOrigin(groupPatientsBy(allPatients, p -> p.getFrom() != null ? p.getFrom().name() : "Otro"));
        patStats.setPatientsByCity(groupPatientsBy(allPatients, p -> p.getCity() != null && !p.getCity().isEmpty() ? p.getCity() : "Otra"));
        patStats.setPatientsByReason(groupPatientsBy(allPatients, p -> p.getReasonForConsultation() != null ? p.getReasonForConsultation().name() : "No especificado"));
        patStats.setPatientsByAppointmentStatus(groupPatientsBy(allPatients, p -> p.getAppointmentStatus() != null ? p.getAppointmentStatus().name() : "No especificado"));

        return patStats;
    }

    private List<StatItemDTO> groupPatientsBy(List<Patient> patients, java.util.function.Function<Patient, String> classifier) {
        Map<String, List<Patient>> grouped = patients.stream().collect(Collectors.groupingBy(classifier));
        List<StatItemDTO> list = new ArrayList<>();
        long total = patients.size();

        for (Map.Entry<String, List<Patient>> entry : grouped.entrySet()) {
            long count = entry.getValue().size();

            List<DrillDownDetailDTO> details = entry.getValue().stream().map(p -> new DrillDownDetailDTO(
                    p.getId(),
                    p.getFullName(),
                    "DNI: " + p.getDni()
            )).toList();

            double percentage = total > 0 ? ((double) count / total) * 100 : 0;
            list.add(new StatItemDTO(entry.getKey(), count, Math.round(percentage * 10.0) / 10.0, details));
        }
        return list;
    }
}