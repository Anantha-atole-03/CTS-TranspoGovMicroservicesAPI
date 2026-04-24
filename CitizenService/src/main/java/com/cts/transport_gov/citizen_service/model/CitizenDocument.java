package com.cts.transport_gov.citizen_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

/**
 * Entity representing documents uploaded by or for a citizen.
 * Maps to the 'citizen_document' table and tracks verification status and storage locations.
 */
@Entity
@Table(name = "citizen_document")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@Slf4j
public class CitizenDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentid") 
    private Long id;

    /** Reference to the owner of the document */
    @Column(name = "citizenid") 
    private Long citizenId;

    /** The category of document (e.g., ID_CARD, PASSPORT) */
    @Column(name = "doc_type") 
    private String docType;

    /** The physical path or cloud URI of the stored file */
    @Column(name = "file_uri") 
    private String fileUri;

    /** Timestamp when the document record was created */
    @Column(name = "uploaded_date") 
    private LocalDateTime uploadedDate;

    /** Current state of document validation (e.g., PENDING, VERIFIED) */
    @Column(name = "verification_status") 
    private String verificationStatus;

    /**
     * Lifecycle hook to initialize timestamps and default status before database insertion.
     */
    @PrePersist
    protected void onCreate() {
        this.uploadedDate = LocalDateTime.now();
        if (this.verificationStatus == null) {
            this.verificationStatus = "PENDING";
        }
        log.info("Registering new document for Citizen ID: {} (Type: {})", citizenId, docType);
    }

    /**
     * Command to log the current document verification state.
     */
    public void logVerificationStatus() {
        log.info("Document ID: {} is currently: {}", id, verificationStatus);
    }
}