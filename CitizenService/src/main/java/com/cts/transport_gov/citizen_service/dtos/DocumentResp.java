package com.cts.transport_gov.citizen_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Document Response details.
 * This class provides the metadata and URI for a specific document associated with an entity.
 */
@Data
@Slf4j
public class DocumentResp {

    /** The unique identifier for the document record */
    private Long documentID;

    /** The identifier of the entity (e.g., citizen or case) this document belongs to */
    private Long entityId;

    /** The category or type of document (e.g., ID_CARD, PASSPORT, PROOF_OF_ADDRESS) */
    private String docType;

    /** The storage URI or path where the file is located */
    private String fileUri;

    /** The current verification state of the document (e.g., PENDING, VERIFIED, REJECTED) */
    private String verificationStatus;

    /** The timestamp indicating when the document was uploaded */
    private LocalDateTime uploadedDate;

    /**
     * Logs the document metadata for auditing or troubleshooting purposes.
     */
    public void logDocumentInfo() {
        log.info("Accessing metadata for Document ID: {} (Type: {})", documentID, docType);
        log.debug("Full Document Response: {}", this.toString());
    }
}