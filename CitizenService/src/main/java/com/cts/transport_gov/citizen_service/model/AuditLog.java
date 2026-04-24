package com.cts.transport_gov.citizen_service.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity representing an Audit Log entry.
 * Tracks system activities for security and compliance requirements.
 */
@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@Slf4j
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId; // Maps to requirement

    /** The ID of the User (Citizen or Lawyer) performing the action */
    private Long userId; 

    /** The type of activity performed (e.g., "REGISTRATION", "UPDATE") */
    private String action; 

    /** The specific system resource affected (e.g., "CITIZEN_PROFILE") */
    private String resource; 

    /** The exact date and time the activity occurred */
    private LocalDateTime timestamp;

    /**
     * Records a log entry regarding the creation of this audit record.
     */
    public void logAuditCreation() {
        log.info("New Audit Entry created for User ID: {} | Action: {} on Resource: {}", 
                 userId, action, resource);
    }

    /**
     * Lifecycle callback to log when the audit entry is successfully saved to the database.
     */
    @PostPersist
    public void onPostPersist() {
        log.debug("AuditLog successfully persisted with ID: {}", auditId);
    }
}