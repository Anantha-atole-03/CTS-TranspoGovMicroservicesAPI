package com.cts.transport_gov.citizen_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.citizen_service.dtos.DocumentResp;
import com.cts.transport_gov.citizen_service.dtos.DocumentUploadReq;
import com.cts.transport_gov.citizen_service.model.AuditLog;
import com.cts.transport_gov.citizen_service.model.CitizenDocument;
import com.cts.transport_gov.citizen_service.repository.AuditLogRepository;
import com.cts.transport_gov.citizen_service.repository.CitizenDocumentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final CitizenDocumentRepository docRepo;
    private final AuditLogRepository auditRepo;

    @Override
    @Transactional
    public String upload(DocumentUploadReq req) {
        log.info("Processing document upload request for Citizen ID: {}", req != null ? req.getOwnerId() : "NULL");
        
        // NULL CHECK: Prevents 500 error if Postman is set to 'Text' instead of 'JSON'
        if (req == null || req.getOwnerId() == null) {
            log.error("Request body is null. Ensure Postman is set to JSON format.");
            throw new IllegalArgumentException("Request body or Owner ID cannot be null");
        }

        try {
            // Build Entity matching your 'citizen_document' table
            CitizenDocument doc = CitizenDocument.builder()
                    .citizenId(req.getOwnerId())
                    .docType(req.getDocType())
                    .fileUri(req.getFileUri())
                    .verificationStatus("PENDING")
                    .build();

            docRepo.save(doc);

            // Audit Trail
            auditRepo.save(AuditLog.builder()
                    .userId(req.getOwnerId())
                    .action("DOCUMENT_UPLOAD")
                    .resource("citizen_document")
                    .timestamp(LocalDateTime.now())
                    .build());

            log.info("Document successfully persisted and audited for ID: {}", req.getOwnerId());
            return "Document uploaded successfully for Citizen ID: " + req.getOwnerId();
            
        } catch (Exception e) {
            log.error("CRITICAL DATABASE ERROR during upload: {}", e.getMessage());
            throw new RuntimeException("Data Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResp> getAllDocuments() {
        log.debug("Executing bulk document retrieval for Officer/Auditor view");
        return docRepo.findAll().stream().map(this::mapToResp).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResp> getByEntity(Long citizenId) {
        log.debug("Retrieving document history for Citizen ID: {}", citizenId);
        return docRepo.findByCitizenId(citizenId).stream().map(this::mapToResp).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String verify(Long docId, String status) {
        log.info("Attempting to verify Document ID: {} with new status: {}", docId, status);
        
        CitizenDocument doc = docRepo.findById(docId)
                .orElseThrow(() -> {
                    log.warn("Verification failed: Document ID {} not found", docId);
                    return new RuntimeException("Document not found");
                });
        
        doc.setVerificationStatus(status.toUpperCase());
        docRepo.save(doc);
        
        log.info("Document ID {} verification status updated to {}", docId, status);
        return "Status updated to " + status;
    }

    /**
     * Maps the internal Entity to a safe Response DTO.
     */
    private DocumentResp mapToResp(CitizenDocument d) {
        DocumentResp resp = new DocumentResp();
        resp.setDocumentID(d.getId());
        resp.setEntityId(d.getCitizenId());
        resp.setDocType(d.getDocType());
        resp.setFileUri(d.getFileUri());
        resp.setVerificationStatus(d.getVerificationStatus());
        resp.setUploadedDate(d.getUploadedDate());
        return resp;
    }

    /**
     * Service Command: Triggers an audit verification for a specific document.
     * Useful for manual re-validation triggers.
     */
    public void executeAuditValidationCommand(Long docId) {
        log.warn("COMMAND: Initiating manual audit validation for Doc ID: {}", docId);
        // Custom logic for manual override or auditor checks
    }
}