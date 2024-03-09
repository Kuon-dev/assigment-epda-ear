package com.epda.model.dto;

import com.epda.model.Appointment;

public class AppointmentDTO {

    private Long id;
    private Long petId;
    private String petName;
    private Long veterinarianId;
    private String veterinarianName;
    private String timeSlot;
    private String status;
    private String diagnosis;
    private String prognosis;
    private String appointmentDate; // Use a String to simplify serialization issues with dates.

    public AppointmentDTO() {}

    public AppointmentDTO(
        Long id,
        Long petId,
        String petName,
        Long veterinarianId,
        String veterinarianName,
        String timeSlot,
        String status,
        String diagnosis,
        String prognosis,
        String appointmentDate
    ) {
        this.id = id;
        this.petId = petId;
        this.petName = petName;
        this.veterinarianId = veterinarianId;
        this.veterinarianName = veterinarianName;
        this.timeSlot = timeSlot;
        this.status = status;
        this.diagnosis = diagnosis;
        this.prognosis = prognosis;
        this.appointmentDate = appointmentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Long getVeterinarianId() {
        return veterinarianId;
    }

    public void setVeterinarianId(Long veterinarianId) {
        this.veterinarianId = veterinarianId;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }

    public void setVeterinarianName(String veterinarianName) {
        this.veterinarianName = veterinarianName;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrognosis() {
        return prognosis;
    }

    public void setPrognosis(String prognosis) {
        this.prognosis = prognosis;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public static AppointmentDTO fromEntity(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPetId(appointment.getPet().getId());
        dto.setPetName(appointment.getPet().getName());
        dto.setVeterinarianId(appointment.getVeterinarian().getId());
        dto.setVeterinarianName(appointment.getVeterinarian().getName());
        dto.setTimeSlot(appointment.getTimeSlot().toString());
        dto.setStatus(appointment.getStatus().toString());
        dto.setDiagnosis(appointment.getDiagnosis());
        dto.setPrognosis(appointment.getPrognosis());
        dto.setAppointmentDate(appointment.getAppointmentDate().toString());
        return dto;
    }
}
