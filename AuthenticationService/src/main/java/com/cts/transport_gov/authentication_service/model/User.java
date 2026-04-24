package com.cts.transport_gov.authentication_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Maintain the definitive source of truth for user identity, credentials, and access levels.
 * Logic: Maps the central 'users' table to the application, enforcing unique identity via email 
 * and managing state through Role and Status enumerations.
 */
@Entity
@Table(name = "users")
@Getter 
@Setter 
@NoArgsConstructor
@Slf4j
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;
    private String password;

    @Enumerated(EnumType.STRING) 
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /**
     * Logs the current state of the User object for debugging or lifecycle tracking.
     * Note: PII and sensitive data (password) are excluded from this log.
     */
    public void logUserStatus() {
        log.info("User Entity State: [ID: {}, Email: {}, Role: {}, Status: {}]", 
                 this.userId, this.email, this.role, this.status);
    }
}