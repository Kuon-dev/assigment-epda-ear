package com.epda.model;

import jakarta.persistence.Entity;
import java.io.Serializable;
// import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Collection;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import jakarta.persistence.CascadeType;

// import com.epda.model.User;

// @DiscriminatorValue("CUSTOMER")
@Entity
@Table(name = "Customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private static final long serialVersionUID = 1L;
    @Column(name = "id", nullable = false, unique = true, length = 10)
    private long id;
    private String name;
    private String email;
    // owns many pets
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true) // Recommended to specify cascade and orphanRemoval
    private Collection<Pet> pets = new ArrayList<>();

    public Customer() {}

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

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
}
