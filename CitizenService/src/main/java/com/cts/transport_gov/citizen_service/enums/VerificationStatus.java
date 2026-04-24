package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * Enumeration representing the possible states of a verification process.
 * Used for validating documents, citizen profiles, and legal credentials.
 */
@Slf4j
public enum VerificationStatus {
    /** The resource is currently awaiting review by an officer */
	PENDING, 
    
    /** The resource has been checked and found to be authentic and valid */
    VERIFIED, 
    
    /** The resource has been reviewed and found to be invalid or fraudulent */
    REJECTED;

    /**
     * Command method to log the current verification state.
     * Useful for tracking progress in the service layer.
     */
    public void logStatus() {
        log.info("Current verification state: {}", this.name());
    }

    /**
     * Specialized command to log a warning if a rejection is processed.
     */
    public void logResultDetails() {
        if (this == REJECTED) {
            log.warn("ALERT: A resource has been marked as REJECTED during verification.");
        } else {
            log.debug("Verification step processed with status: {}", this.name());
        }
    }
}