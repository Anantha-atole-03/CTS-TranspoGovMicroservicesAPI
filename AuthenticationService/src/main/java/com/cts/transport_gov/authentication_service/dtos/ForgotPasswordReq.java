package com.cts.transport_gov.authentication_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for password recovery requests.
 * Captures the email address to trigger the reset token generation process.
 */
@Data
@Slf4j
public class ForgotPasswordReq {

    /** The email address associated with the account requiring a password reset */
    private String email;

    /**
     * COMMAND: Logs the initiation of a password recovery attempt.
     * This helps in monitoring potential brute-force attempts on the reset service.
     */
    public void logRequest() {
        if (this.email != null && !this.email.isEmpty()) {
            log.info("Password recovery process initiated for email: {}", this.email);
        } else {
            log.warn("Password recovery requested with a null or empty email address.");
        }
    }

    /**
     * COMMAND: Logs a debug event when the DTO is successfully validated.
     */
    public void logValidationSuccess() {
        log.debug("ForgotPasswordReq validation successful for: {}", this.email);
    }
}