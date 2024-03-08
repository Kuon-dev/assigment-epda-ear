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
@Table(name = "Veterinarian")
public class Veterinarian extends User implements Serializable {

    public Veterinarian() {}
}
