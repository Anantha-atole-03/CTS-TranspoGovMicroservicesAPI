package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * Represents the potential results of a compliance check or verification process.
 */
@Slf4j
public enum ComplianceResult {
    /** The check met all requirements successfully */
    PASSED, 
    
    /** The check did not meet essential requirements */
    FAILED, 
    
    /** The check passed but identified potential issues */
    WARNING, 
    
    /** The entity is fully compliant with regulations */
    COMPLIANT, 
    
    /** The entity is not compliant with regulations */
    NON_COMPLIANT;

    /**
     * Utility command to log the result being applied.
     * Useful for auditing decision logic in the service layer.
     */
    public void logResult() {
        log.info("Compliance check concluded with result: {}", this.name());
    }
}