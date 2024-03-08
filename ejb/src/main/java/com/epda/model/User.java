package com.epda.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true, length = 10)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "passsword")
    private String password;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = this.updatedAt = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Date();
    }
}
