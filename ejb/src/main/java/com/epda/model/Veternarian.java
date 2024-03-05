package com.epda.model;

import jakarta.persistence.Entity;
import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
// import com.epda.model.User;

@Entity
@DiscriminatorValue("VETERNARIAN")
public class Veternarian extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    // @OneToMany

    public Veternarian() {}

    public Veternarian(String id, String password) {
        super(id, password);
    }
}
