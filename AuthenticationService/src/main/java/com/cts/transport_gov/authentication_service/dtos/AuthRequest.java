package com.cts.transport_gov.authentication_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for authentication requests.
 * Used to capture user credentials during login.
 */
@Data
@Slf4j
public class AuthRequest {

    /** The unique email address used as the username */
    private String email;

    /** The plain-text password provided for authentication */
    private String password;

    /**
     * COMMAND: Logs the authentication attempt for a specific email.
     * Security Rule: This method ensures the password is never leaked into the system logs.
     */
    public void logAuthAttempt() {
        if (this.email != null && !this.email.isEmpty()) {
            log.info("Authentication attempt initiated for user: {}", this.email);
        } else {
            log.warn("Authentication attempt initiated with an empty email field.");
        }
    }

    /**
     * Command to verify object readiness for processing.
     */
    public void logRequestStatus() {
        log.debug("AuthRequest payload received and ready for validation.");
    }
}