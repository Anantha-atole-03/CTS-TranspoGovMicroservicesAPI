package com.cts.transport_gov.authentication_service.model;

import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Govern the operational lifecycle and access eligibility of user accounts.
 * Logic: Dictates whether the system should permit authentication or restrict access 
 * based on administrative actions or verification states.
 */
@Slf4j
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    PENDING,
    SUSPENDED,
    DELETED;

    /**
     * Logs the application of a status state to a specific context.
     */
    public void logStatusTransition() {
        log.info("Account state recognized as: {}", this.name());
    }
}