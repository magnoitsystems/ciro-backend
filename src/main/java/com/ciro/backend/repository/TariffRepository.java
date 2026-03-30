package com.ciro.backend.repository;

import com.ciro.backend.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {

    @Query("SELECT t FROM Tariff t WHERE " +
            "(:keyword IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:minPesos IS NULL OR t.amountPesos >= :minPesos) AND " +
            "(:minDollars IS NULL OR t.amountDollars >= :minDollars)")
    List<Tariff> searchTariffs(
            @Param("keyword") String keyword,
            @Param("minPesos") BigDecimal minPesos,
            @Param("minDollars") BigDecimal minDollars
    );
}