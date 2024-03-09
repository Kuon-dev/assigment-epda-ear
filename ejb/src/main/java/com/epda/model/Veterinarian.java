package com.epda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.NoArgsConstructor;

// import com.epda.model.User;

// import com.epda.model.User;

@NoArgsConstructor
@Entity
@Table(name = "Veterinarian")
public class Veterinarian extends User implements Serializable {

    @OneToMany(mappedBy = "veterinarian", fetch = FetchType.LAZY)
    private List<Appointment> appointments;
}
