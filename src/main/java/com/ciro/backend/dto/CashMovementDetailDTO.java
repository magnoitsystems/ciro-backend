package com.ciro.backend.dto;

import com.ciro.backend.enums.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CashMovementDetailDTO {
    private Long id;
    private BigDecimal amount;
    private CurrencyType currencyType;
    private PaymentMethod paymentMethod;
    private LocalDateTime movementDate;
    private CashMovementType type;
    private String observations;
    private Long doctorId;

    private Object relatedEntity;

    private List<PercentageSplitDTO> suggestedSplits;

    public static class PercentageSplitDTO {
        private String label;
        private BigDecimal doctorAmount;
        private BigDecimal clinicAmount;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public BigDecimal getDoctorAmount() {
            return doctorAmount;
        }

        public void setDoctorAmount(BigDecimal doctorAmount) {
            this.doctorAmount = doctorAmount;
        }

        public BigDecimal getClinicAmount() {
            return clinicAmount;
        }

        public void setClinicAmount(BigDecimal clinicAmount) {
            this.clinicAmount = clinicAmount;
        }
    }
}