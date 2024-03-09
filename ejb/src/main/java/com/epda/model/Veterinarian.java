package com.epda.model;

import com.epda.model.enums.Expertise;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Veterinarian")
public class Veterinarian extends User implements Serializable {

    @OneToMany(mappedBy = "veterinarian", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @Enumerated(EnumType.STRING)
    @Column(name = "expertise")
    private Expertise expertise;
}
