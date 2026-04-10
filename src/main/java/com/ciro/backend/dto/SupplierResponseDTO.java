package com.ciro.backend.dto;

public class SupplierResponseDTO {
    private Long id;
    private String fullName;
    private String address;
    private String city;
    private String dni;
    private String observations;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}