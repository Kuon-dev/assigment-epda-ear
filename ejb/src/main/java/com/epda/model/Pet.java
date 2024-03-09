package com.epda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
// import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Pet")
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // foreign key customer id
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "breed", nullable = false, length = 50)
    private String breed;

    @Column(name = "age", nullable = false)
    private int age;

    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    public Pet(String name, String type, String breed, int age) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
    }
}
