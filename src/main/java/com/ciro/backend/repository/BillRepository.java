package com.ciro.backend.repository;

import com.ciro.backend.entity.Bill;
import com.ciro.backend.enums.BillStatus;
import com.ciro.backend.enums.BillType;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.OriginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByOrderByBillDateAsc();

    List<Bill> findByBillTypeOrderByBillDateAsc(BillType billType);

    List<Bill> findByFromOrderByBillDateAsc(OriginType from);

    List<Bill> findByBillTypeAndFromOrderByBillDateAsc(BillType billType, OriginType from);

    List<Bill> findByStatusAndBillDateBetweenOrderByBillDateAsc(
            BillStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Bill b WHERE b.status = 'PAGADO' AND b.currencyType = :currency AND b.billDate BETWEEN :startDate AND :endDate")
    BigDecimal sumExpensesByCurrencyAndDate(@Param("currency") CurrencyType currency, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
