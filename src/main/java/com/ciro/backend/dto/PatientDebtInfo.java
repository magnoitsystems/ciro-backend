package com.ciro.backend.dto;

import com.ciro.backend.entity.VoucherDetail;
import java.math.BigDecimal;
import java.util.List;

public class PatientDebtInfo {
    private BigDecimal debtPesos;
    private BigDecimal debtDolares;
    private boolean overdue;
    private List<VoucherDetail> unpaidDetails;

    public PatientDebtInfo(BigDecimal debtPesos, BigDecimal debtDolares, boolean overdue, List<VoucherDetail> unpaidDetails) {
        this.debtPesos = debtPesos;
        this.debtDolares = debtDolares;
        this.overdue = overdue;
        this.unpaidDetails = unpaidDetails;
    }

    public BigDecimal getDebtPesos() { return debtPesos; }
    public BigDecimal getDebtDolares() { return debtDolares; }
    public boolean isOverdue() { return overdue; }
    public List<VoucherDetail> getUnpaidDetails() { return unpaidDetails; }
}