package com.cts.transport_gov.authentication_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Persist security-relevant events and administrative actions to the database.
 * Logic: Captures the 'who', 'what', and 'when' for compliance and forensic analysis.
 */
@Entity
@Getter @Setter
@Slf4j
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    @Column(name = "user_id")
    private Long userId;

    private String action;
    private String resource;
    private LocalDateTime timestamp;

    /**
     * Logs the creation of an audit entry.
     * Use this to verify that the auditing system is capturing events in real-time.
     */
    public void logAuditEntry() {
        log.debug("Audit record created: Action='{}' on Resource='{}' by UserID={}", 
                  this.action, this.resource, this.userId);
    }
}