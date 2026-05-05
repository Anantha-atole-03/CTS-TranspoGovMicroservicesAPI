package com.cts.transport_gov.authentication_service.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DocumentUploadReq {

    /** Unique identifier of the document owner (Maps to citizenid) */
    private Long citizenId;

    /** Role of the individual uploading or owning the document */
    private String role;

    /** The category of the document (e.g., PASSPORT, NATIONAL_ID) */
    private String docType;

    /** The storage location or URI where the file is hosted */
    private String fileUri;

}