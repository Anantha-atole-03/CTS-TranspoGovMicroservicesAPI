package com.cts.transport_gov.authentication_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for providing a safe, filtered view of user profile data.
 * Transmits identity details while ensuring sensitive credentials remain internal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserResponse {

    /** Unique identifier for the user entity */
    private Long userId;

    /** Full name of the user */
    private String name;

    /** Primary email address */
    private String email;

    /** Assigned system role (e.g., ADMINISTRATOR, CITIZEN) */
    private String role;

    /** Current account status (e.g., ACTIVE, PENDING) */
    private String status;

    /**
     * COMMAND: Logs the metadata of the user profile being returned.
     * This helps in auditing which profiles are being accessed by the system.
     */
    public void logResponseData() {
        log.info("UserResponse generated for UserId: {} [Role: {}, Status: {}]", 
                 this.userId, 
                 this.role != null ? this.role : "N/A", 
                 this.status != null ? this.status : "N/A");
    }

    /**
     * COMMAND: Logs a warning if the response data is being sent for an inactive or blocked user.
     */
    public void logAccountAlert() {
        if ("BLOCKED".equalsIgnoreCase(this.status) || "INACTIVE".equalsIgnoreCase(this.status)) {
            log.warn("Accessing profile data for restricted User ID: {} with status: {}", this.userId, this.status);
        }
    }
}