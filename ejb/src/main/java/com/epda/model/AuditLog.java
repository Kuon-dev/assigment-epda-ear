package com.epda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "operation")
    private String operation;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    // Constructors, getters, and setters

    public AuditLog(String entityName, Long entityId, String operation) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.operation = operation;
        this.timestamp = LocalDateTime.now();
    }
    // standard getters and setters
}
