package com.epda.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

// import com.epda.model.User;

// import com.epda.model.User;

@Entity
@Table(name = "Veternarian")
public class Veternarian implements Serializable {

    @Id
    @Column(name = "id")
    private String name;

    private String email;
    private String password;

    public Veternarian() {}

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
