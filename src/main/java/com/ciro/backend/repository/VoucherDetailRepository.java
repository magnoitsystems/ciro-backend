package com.ciro.backend.repository;
import com.ciro.backend.entity.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Long> {
    List<VoucherDetail> findByVoucherId(Long voucherId);
}