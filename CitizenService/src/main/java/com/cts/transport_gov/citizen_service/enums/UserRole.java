package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * Enumeration representing the various user roles within the JusticeGov system.
 * Used for access control, auditing, and defining system responsibilities.
 */
@Slf4j
public enum UserRole {
    /** General public user utilizing transport services */
	CITIZEN_PASSENGER, 
    
    /** Operational staff managing transportation logistics */
    TRANSPORT_OFFICER, 
    
    /** Overseer of specific justice programs and initiatives */
    PROGRAM_MANAGER, 
    
    /** System-wide administrator with full configuration access */
    ADMINISTRATOR, 
    
    /** Staff responsible for verifying regulatory and legal adherence */
    COMPLIANCE_OFFICER, 
    
    /** External or internal official performing system audits */
    GOVERNMENT_AUDITOR;

    /**
     * Command method to log when a specific role initiates an action.
     * Useful for security tracing and debugging authorization logic.
     */
    public void logRoleActivity() {
        log.info("Role activity detected for: {}", this.name());
    }

    /**
     * Command to perform a high-priority audit log for privileged roles.
     */
    public void logPrivilegedAccess() {
        if (this == ADMINISTRATOR || this == GOVERNMENT_AUDITOR) {
            log.warn("Privileged system access by role: {}", this.name());
        } else {
            log.debug("Standard system access by role: {}", this.name());
        }
    }
}