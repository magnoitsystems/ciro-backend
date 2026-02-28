package com.ciro.backend.repository;

import com.ciro.backend.entity.Bill;
import com.ciro.backend.enums.BillType;
import com.ciro.backend.enums.OriginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByOrderByBillDateAsc();

    List<Bill> findByBillTypeOrderByBillDateAsc(BillType billType);

    List<Bill> findByFromOrderByBillDateAsc(OriginType from);

    List<Bill> findByBillTypeAndFromOrderByBillDateAsc(BillType billType, OriginType from);
}
