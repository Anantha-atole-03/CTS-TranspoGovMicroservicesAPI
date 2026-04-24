package com.cts.transport_gov.authentication_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for returning authentication results.
 * Contains the JWT token, user identification, and authorization role.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AuthResponse {
    
    /** The JWT bearer token for subsequent authenticated requests */
    private String token;
    
    /** The email of the authenticated user */
    private String email;
    
    /** The assigned security role (e.g., ADMINISTRATOR, CITIZEN) */
    private String role;
    
    /** Operational status code or keyword */
    private String status;
    
    /** Descriptive message regarding the authentication result */
    private String message;

    /**
     * COMMAND: Logs the response details before sending to the client.
     * Note: The sensitive JWT token is intentionally excluded from logs for security.
     */
    public void logResponseStatus() {
        log.info("AuthResponse generated for User: {} | Role: {} | Status: {}", 
                 this.email, this.role, this.status);
    }

    /**
     * COMMAND: Logs a warning if the authentication response indicates a failure.
     */
    public void logFailureIfPresent() {
        if ("FAILURE".equalsIgnoreCase(this.status) || "ERROR".equalsIgnoreCase(this.status)) {
            log.warn("AuthResponse contains failure message: {}", this.message);
        }
    }
}