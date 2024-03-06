package com.epda.model;

import jakarta.persistence.Entity;
import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
// import com.epda.model.User;

@Entity
@Table(name = "Receptionist")
public class Receptionist implements Serializable {

    @Id
    @Column(name = "id")
    private String name;
    private String email;
    private String password;

    public Receptionist() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

