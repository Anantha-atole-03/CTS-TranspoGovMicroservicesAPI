package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines the access levels and responsibilities for users within the JusticeGov system.
 * Used for Role-Based Access Control (RBAC) and auditing.
 */
@Slf4j
public enum Role {
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
    public void logRoleAccess() {
        log.info("Access granted for security role: {}", this.name());
    }

    /**
     * Command to perform an audit-level log for high-privilege roles.
     */
    public void logPrivilegedAction() {
        if (this == ADMINISTRATOR || this == GOVERNMENT_AUDITOR) {
            log.warn("Privileged action initiated by high-level role: {}", this.name());
        } else {
            log.debug("Standard action initiated by role: {}", this.name());
        }
    }
}