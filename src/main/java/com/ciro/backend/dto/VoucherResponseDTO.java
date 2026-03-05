package com.ciro.backend.dto;

import com.ciro.backend.enums.CurrencyType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class VoucherResponseDTO {
    private Long voucherId;
    private LocalDate date;
    private BigDecimal totalAmountPesos;   // ¡NUEVO!
    private BigDecimal totalAmountDollars;

    public VoucherResponseDTO(Long voucherId, LocalDate date, BigDecimal totalAmountPesos, BigDecimal totalAmountDollars) {
        this.voucherId = voucherId;
        this.date = date;
        this.totalAmountPesos = totalAmountPesos;
        this.totalAmountDollars = totalAmountDollars;
    }

    public Long getVoucherId() { return voucherId; }
    public void setVoucherId(Long voucherId) { this.voucherId = voucherId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public BigDecimal getTotalAmountPesos() { return totalAmountPesos; }
    public BigDecimal getTotalAmountDollars() { return totalAmountDollars; }

    public void setTotalAmountDollars(BigDecimal totalAmountDollars) {
        this.totalAmountDollars = totalAmountDollars;
    }
    public void setTotalAmountPesos(BigDecimal totalAmountPesos) {
        this.totalAmountPesos = totalAmountPesos;
    }
}