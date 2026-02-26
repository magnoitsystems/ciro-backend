package com.ciro.backend.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SupplierDTO {
    private String fullName;
    private String address;
    private String city;
    private String dni;
    private String observations;
}
