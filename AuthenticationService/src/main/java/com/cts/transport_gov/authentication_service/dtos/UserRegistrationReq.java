package com.cts.transport_gov.authentication_service.dtos;


import com.cts.transport_gov.authentication_service.model.UserRole;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for capturing new user profile data.
 * Facilitates the registration flow by transporting validated user input.
 */
@Data
@Slf4j
public class UserRegistrationReq {

    private String name;
    private String email;
    private String phone;
    private String password;
    private UserRole role; 

    /**
     * COMMAND: Logs the intent to register a new user.
     * Security Rule: To maintain privacy compliance (GDPR/SOC2), this method 
     * logs only the email and role, explicitly ignoring the password and phone number.
     */
    public void logRegistrationAttempt() {
        log.info("New registration request received for email: {} with assigned role: {}", 
                 this.email, 
                 this.role != null ? this.role.name() : "UNDEFINED");
    }

    /**
     * COMMAND: Logs a warning if the registration request is missing a role assignment.
     */
    public void logRoleCheck() {
        if (this.role == null) {
            log.warn("Registration request for {} initiated without a specified UserRole.", this.email);
        } else {
            log.debug("Role validation passed for user: {}", this.email);
        }
    }
}