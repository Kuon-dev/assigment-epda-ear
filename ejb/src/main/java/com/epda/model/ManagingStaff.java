package com.epda.model;

import jakarta.persistence.Entity;
import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import com.epda.model.User;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Inheritance;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name = "ManagingStaff")
public class ManagingStaff extends User implements Serializable {

  public ManagingStaff() {}
}

