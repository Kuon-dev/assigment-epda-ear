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
public class Receptionist extends User implements Serializable {

  public Receptionist() {
  }
}

