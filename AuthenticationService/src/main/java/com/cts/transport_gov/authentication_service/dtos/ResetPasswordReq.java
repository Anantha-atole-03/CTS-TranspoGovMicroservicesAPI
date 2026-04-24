package com.cts.transport_gov.authentication_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for processing password updates.
 * Contains the user identity, the security token, and the replacement credential.
 */
@Data
@Slf4j
public class ResetPasswordReq {

    /** The email address of the account being recovered */
    private String email;        
    
    /** The unique token issued during the forgot-password flow */
    private String resetToken;   
    
    /** The new plain-text password to be encrypted and saved */
    private String newPassword;

    /**
     * COMMAND: Logs the execution of a password reset attempt.
     * Security Rule: Logs the presence of the token but never the 'newPassword' 
     * to maintain strict security and PCI/SOC2 compliance.
     */
    public void logResetAttempt() {
        log.info("Processing password reset attempt for: {} | Token status: {}", 
                 this.email, 
                 (this.resetToken != null && !this.resetToken.isEmpty() ? "PROVIDED" : "MISSING"));
    }

    /**
     * COMMAND: Logs a warning if the request is submitted with incomplete identifiers.
     */
    public void logIncompleteRequest() {
        if (this.email == null || this.resetToken == null) {
            log.warn("ResetPasswordReq received with missing critical fields for email: {}", this.email);
        }
    }
}