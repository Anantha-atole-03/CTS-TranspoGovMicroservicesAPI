package com.cts.transport_gov.citizen_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for Document Upload Requests.
 * This class captures the metadata required to link a file URI to a specific owner and role.
 */
@Data
@Slf4j
public class DocumentUploadReq {

    /** Unique identifier of the document owner (Maps to citizenid) */
    private Long ownerId;

    /** Role of the individual uploading or owning the document */
    private String role;

    /** The category of the document (e.g., PASSPORT, NATIONAL_ID) */
    private String docType;

    /** The storage location or URI where the file is hosted */
    private String fileUri;

    /**
     * Utility method to log the upload request details for tracking and auditing.
     */
    public void logUploadRequest() {
        log.info("Initiating document upload for Owner ID: {} with Role: {}", ownerId, role);
        log.debug("Upload Details - Type: {}, URI: {}", docType, fileUri);
    }
}