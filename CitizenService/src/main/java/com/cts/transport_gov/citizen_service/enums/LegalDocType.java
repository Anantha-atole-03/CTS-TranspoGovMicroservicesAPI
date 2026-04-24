package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * Enumeration defining the specific types of legal documents 
 * accepted within the JusticeGov system.
 */
@Slf4j
public enum LegalDocType {
    
    /** National or Government issued Identity Proof */
    ID_PROOF, 
    
    /** Official certificate issued by the Bar Council for legal practitioners */
    BAR_CERTIFICATE;

    /**
     * Command method to log the document type being handled.
     * Useful for auditing document upload and verification steps.
     */
    public void logDocumentType() {
        log.info("Processing legal document of type: {}", this.name());
    }

    /**
     * Utility command to verify if the type is related to professional certification.
     */
    public void logProfessionalStatus() {
        if (this == BAR_CERTIFICATE) {
            log.debug("Professional certification type detected: {}", this.name());
        }
    }
}