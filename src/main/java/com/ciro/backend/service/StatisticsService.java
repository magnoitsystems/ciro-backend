package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.repository.BillRepository;
import com.ciro.backend.repository.PatientRepository;
import com.ciro.backend.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired private ReceiptRepository receiptRepository;
    @Autowired private BillRepository billRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private PatientService patientService;

    public StatisticsResponseDTO getDashboardStatistics() {
        StatisticsResponseDTO response = new StatisticsResponseDTO();

        YearMonth currentMonth = YearMonth.now();
        LocalDate startCurrent = currentMonth.atDay(1);
        LocalDate endCurrent = currentMonth.atEndOfMonth();

        YearMonth prevMonth = currentMonth.minusMonths(1);
        LocalDate startPrev = prevMonth.atDay(1);
        LocalDate endPrev = prevMonth.atEndOfMonth();

        response.setFinancial(buildFinancialStats(startCurrent, endCurrent, startPrev, endPrev));

        response.setPatients(buildPatientStats());

        response.setImplantsThisMonth(0L);

        return response;
    }

    private FinancialStatsDTO buildFinancialStats(LocalDate startCurr, LocalDate endCurr, LocalDate startPrev, LocalDate endPrev) {
        FinancialStatsDTO finStats = new FinancialStatsDTO();

        finStats.setCurrentMonthIncomePesos(getSafeDecimal(receiptRepository.sumIncomeByCurrencyAndDate(CurrencyType.PESOS, startCurr, endCurr)));
        finStats.setCurrentMonthIncomeDollars(getSafeDecimal(receiptRepository.sumIncomeByCurrencyAndDate(CurrencyType.DOLARES, startCurr, endCurr)));
        finStats.setCurrentMonthExpensesPesos(getSafeDecimal(billRepository.sumExpensesByCurrencyAndDate(CurrencyType.PESOS, startCurr, endCurr)));
        finStats.setCurrentMonthExpensesDollars(getSafeDecimal(billRepository.sumExpensesByCurrencyAndDate(CurrencyType.DOLARES, startCurr, endCurr)));

        finStats.setPreviousMonthIncomePesos(getSafeDecimal(receiptRepository.sumIncomeByCurrencyAndDate(CurrencyType.PESOS, startPrev, endPrev)));
        finStats.setPreviousMonthIncomeDollars(getSafeDecimal(receiptRepository.sumIncomeByCurrencyAndDate(CurrencyType.DOLARES, startPrev, endPrev)));

        List<Object[]> breakdownData = receiptRepository.sumIncomeByCurrencyAndPaymentMethod(startCurr, endCurr);
        List<StatItemDTO> breakdownList = new ArrayList<>();


        for (Object[] row : breakdownData) {
            String currency = row[0].toString();
            String method = row[1].toString();
            BigDecimal amount = (BigDecimal) row[2];

            BigDecimal totalForCurrency = currency.equals("PESOS") ? finStats.getCurrentMonthIncomePesos() : finStats.getCurrentMonthIncomeDollars();
            double percentage = 0.0;
            if (totalForCurrency.compareTo(BigDecimal.ZERO) > 0) {
                percentage = amount.divide(totalForCurrency, 4, RoundingMode.HALF_UP).doubleValue() * 100;
            }

            breakdownList.add(new StatItemDTO(currency + " - " + method, amount, Math.round(percentage * 10.0) / 10.0));
        }
        finStats.setIncomeBreakdown(breakdownList);

        return finStats;
    }

    private PatientStatsDTO buildPatientStats() {
        PatientStatsDTO patStats = new PatientStatsDTO();

        long totalPatients = patientRepository.count();
        long totalDebtors = patientService.getDebtorPatients().size();
        long totalNonDebtors = totalPatients - totalDebtors;

        patStats.setTotalPatients(totalPatients);
        patStats.setTotalDebtors(totalDebtors);
        patStats.setTotalNonDebtors(totalNonDebtors);

        List<Object[]> originData = patientRepository.countPatientsByOrigin();
        patStats.setPatientsByOrigin(mapToStatItems(originData, totalPatients));

        List<Object[]> cityData = patientRepository.countPatientsByCity();
        patStats.setPatientsByCity(mapToStatItems(cityData, totalPatients));

        return patStats;
    }

    private List<StatItemDTO> mapToStatItems(List<Object[]> data, long totalEntities) {
        List<StatItemDTO> list = new ArrayList<>();
        if (totalEntities == 0) return list;

        for (Object[] row : data) {
            String label = row[0] != null ? row[0].toString() : "Otro";
            Long count = ((Number) row[1]).longValue();
            double percentage = ((double) count / totalEntities) * 100;

            percentage = Math.round(percentage * 10.0) / 10.0;

            list.add(new StatItemDTO(label, count, percentage));
        }
        return list;
    }

    private BigDecimal getSafeDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}