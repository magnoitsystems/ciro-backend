package com.ciro.backend.dto;

import com.ciro.backend.enums.HealthInsurance;
import com.ciro.backend.enums.PatientFrom;
import java.time.LocalDate;

public class PatientUpdateDTO {

    private String fullName;
    private String address;
    private String city;
    private String phone;
    private LocalDate birthDate;
    private HealthInsurance obraSocial;
    private PatientFrom from;
    private String observations;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public HealthInsurance getObraSocial() { return obraSocial; }
    public void setObraSocial(HealthInsurance obraSocial) { this.obraSocial = obraSocial; }

    public PatientFrom getFrom() { return from; }
    public void setFrom(PatientFrom from) { this.from = from; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}