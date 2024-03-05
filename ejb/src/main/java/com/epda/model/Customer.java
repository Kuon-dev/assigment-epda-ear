package com.epda.model;

import jakarta.persistence.Entity;
import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;

// import com.epda.model.User;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User implements Serializable {

    public Customer() {}

    public Customer(String id, String password) {
        super(id, password);
    }
}
