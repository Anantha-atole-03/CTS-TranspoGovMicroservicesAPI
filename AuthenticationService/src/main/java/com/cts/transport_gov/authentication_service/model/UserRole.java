package com.cts.transport_gov.authentication_service.model;

import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Define the authoritative set of access levels and permissions within the JusticeGov ecosystem.
 * Logic: Controls the functional scope of users, ranging from basic citizen access to high-level auditing and administration.
 */
@Slf4j
public enum UserRole {
    CITIZEN_PASSENGER, 
    TRANSPORT_OFFICER, 
    PROGRAM_MANAGER, 
    ADMINISTRATOR, 
    COMPLIANCE_OFFICER, 
    GOVERNMENT_AUDITOR;

    /**
     * Logs the selection or assignment of a specific role.
     */
    public void logRoleAccess() {
        log.info("Role validation triggered for: {}", this.name());
    }
}