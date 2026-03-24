package com.ciro.backend.repository;

import com.ciro.backend.entity.Receipt;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByPatientIdOrderByReceiptDateDesc(Long patientId);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Receipt r WHERE r.currencyType = :currency AND r.receiptDate BETWEEN :startDate AND :endDate")
    BigDecimal sumIncomeByCurrencyAndDate(@Param("currency") CurrencyType currency, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.paymentMethod = :method AND r.receiptDate BETWEEN :startDate AND :endDate")
    long countByPaymentMethodAndDate(@Param("method") PaymentMethod method, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.receiptDate BETWEEN :startDate AND :endDate")
    long countTotalReceiptsInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT r.currencyType, r.paymentMethod, SUM(r.amount) FROM Receipt r WHERE r.receiptDate BETWEEN :startDate AND :endDate GROUP BY r.currencyType, r.paymentMethod")
    List<Object[]> sumIncomeByCurrencyAndPaymentMethod(@Param("startDate") java.time.LocalDate startDate, @Param("endDate") java.time.LocalDate endDate);
}