package com.ciro.backend.repository;
import com.ciro.backend.entity.CurrentAccount;
import com.ciro.backend.enums.CurrencyType;
import com.ciro.backend.enums.CurrentAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, Long> {
    Optional<CurrentAccount> findTopByPatientIdAndCurrencyOrderByIdDesc(Long patientId, CurrencyType currency);

    List<CurrentAccount> findByPatientIdOrderByIdDesc(Long patientId);

    List<CurrentAccount> findByPatientIdAndTypeOrderByIdDesc(Long patientId, CurrentAccountType type);
}