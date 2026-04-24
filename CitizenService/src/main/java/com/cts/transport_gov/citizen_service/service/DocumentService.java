package com.cts.transport_gov.citizen_service.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.transport_gov.citizen_service.dtos.DocumentResp;
import com.cts.transport_gov.citizen_service.dtos.DocumentUploadReq;

import java.util.List;

/**
 * Service interface for Document management.
 * Handles the registration, validation, and auditing of legal and transport documents.
 */
public interface DocumentService {

    // Static logger for service-level activity tracking
    Logger log = LoggerFactory.getLogger(DocumentService.class);

    /**
     * Registers and validates transport documents (IDProof/TransportCard).
     * @param req The document upload request payload.
     * @return A confirmation message or file reference.
     */
    String upload(DocumentUploadReq req);
    
    /**
     * Conducts audits and records verification findings.
     * @param docId The unique identifier of the document.
     * @param status The new verification status (e.g., VERIFIED, REJECTED).
     * @return Status message of the operation.
     */
    String verify(Long docId, String status);
    
    /**
     * Maintains profiles and allows viewing of document history for a specific citizen.
     * @param citizenId The ID of the document owner.
     * @return List of documents associated with the entity.
     */
    List<DocumentResp> getByEntity(Long citizenId);
    
    /**
     * Comprehensive list for Officers and Auditors to review all system documents.
     * @return List of all documents in the system.
     */
    List<DocumentResp> getAllDocuments();

    /**
     * Command method to log the start of a document workflow.
     * @param action The operation name (e.g., "UPLOAD_ATTEMPT").
     * @param detail Metadata such as Document ID or Citizen ID.
     */
    default void logDocumentAction(String action, String detail) {
        log.info("DocumentService executing {}: Detail [{}]", action, detail);
    }

    /**
     * Command to log verification results specifically for audit trails.
     */
    default void logAuditResult(Long docId, String status) {
        if ("REJECTED".equalsIgnoreCase(status)) {
            log.warn("AUDIT ALERT: Document ID {} has been REJECTED.", docId);
        } else {
            log.info("Audit successful for Document ID {}: New status is {}.", docId, status);
        }
    }
}