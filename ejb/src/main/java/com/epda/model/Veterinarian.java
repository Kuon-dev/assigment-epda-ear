package com.epda.model;

import com.epda.model.enums.Expertise;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
// import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.NoArgsConstructor;

// @Getter
// @Setter
@NoArgsConstructor
@Entity
@Table(name = "Veterinarian")
public class Veterinarian extends User implements Serializable {

    @OneToMany(mappedBy = "veterinarian", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @ElementCollection(targetClass = Expertise.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "veterinarian_expertise",
        joinColumns = @JoinColumn(name = "veterinarian_id")
    )
    @Column(name = "expertise")
    @OrderColumn(name = "expertise_order") // This column ensures the list order is maintained
    private List<Expertise> expertises; // Renamed to plural to reflect multiple possible values

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Expertise> getExpertises() {
        return expertises;
    }

    public void setExpertises(List<Expertise> expertises) {
        this.expertises = expertises;
    }
}
