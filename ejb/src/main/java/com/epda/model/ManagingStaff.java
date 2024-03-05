package com.epda.model;

import jakarta.persistence.Entity;
import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;

// import com.epda.model.User;

@Entity
@DiscriminatorValue("MANAGING_STAFF")
public class ManagingStaff extends User implements Serializable {

    public ManagingStaff() {}

    public ManagingStaff(String id, String password) {
        super(id, password);
    }
}

