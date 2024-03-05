package com.epda.model;

import jakarta.persistence.Entity;
import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
// import com.epda.model.User;

@Entity
@DiscriminatorValue("RECEPTIONIST")
public class Receptionist extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    // @OneToMany

    public Receptionist() {}

    public Receptionist(String id, String password) {
        super(id, password);
    }
}
