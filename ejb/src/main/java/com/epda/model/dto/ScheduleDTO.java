package com.epda.model.dto;

import com.epda.model.Schedule;

public class ScheduleDTO {

    public String vet_id;
    public String shift;
    public String date;

    public ScheduleDTO(Schedule schedule) {
        this.vet_id = schedule.getVeterinarian().getId().toString();
        this.shift = schedule.getShift().toString();
        this.date = schedule.getDate().toString();
    }

    public ScheduleDTO(String vet_id, String shift, String date) {
        this.vet_id = vet_id;
        this.shift = shift;
        this.date = date;
    }

    // default constructor
    public ScheduleDTO() {}

    public String getVet_id() {
        return vet_id;
    }

    public void setVet_id(String vet_id) {
        this.vet_id = vet_id;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
