package com.ciro.backend.dto;

import com.ciro.backend.enums.AppointmentStatus;
import com.ciro.backend.enums.DocumentType;
import com.ciro.backend.enums.HealthInsurance;
import com.ciro.backend.enums.PatientFrom;
import com.ciro.backend.enums.ReasonForConsultation;
import java.time.LocalDate;

public class PatientCreateDTO {
    private String fullName;
    private String address;
    private String city;
    private String phone;
    private LocalDate birthDate;
    private DocumentType documentType;
    private String dni;
    private HealthInsurance obraSocial;
    private PatientFrom from;
    private String observations;
    private Long createdById;
    private ReasonForConsultation reasonForConsultation;
    private AppointmentStatus appointmentStatus;

    public ReasonForConsultation getReasonForConsultation() { return reasonForConsultation; }
    public void setReasonForConsultation(ReasonForConsultation reasonForConsultation) { this.reasonForConsultation = reasonForConsultation; }
    public AppointmentStatus getAppointmentStatus() { return appointmentStatus; }
    public void setAppointmentStatus(AppointmentStatus appointmentStatus) { this.appointmentStatus = appointmentStatus; }
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
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public HealthInsurance getObraSocial() { return obraSocial; }
    public void setObraSocial(HealthInsurance obraSocial) { this.obraSocial = obraSocial; }
    public PatientFrom getFrom() { return from; }
    public void setFrom(PatientFrom from) { this.from = from; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
}