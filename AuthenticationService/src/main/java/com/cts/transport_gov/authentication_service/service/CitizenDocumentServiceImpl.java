package com.cts.transport_gov.authentication_service.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.authentication_service.dto.CitizenDocumentResponse;
import com.cts.transport_gov.authentication_service.dto.CitizenDocumentVerifyRequest;
import com.cts.transport_gov.authentication_service.dto.DocumentUploadReq;
import com.cts.transport_gov.authentication_service.enums.DocumentVerificationStatus;
import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.model.Citizen;
import com.cts.transport_gov.authentication_service.model.CitizenDocument;
import com.cts.transport_gov.authentication_service.respository.AuditLogRepository;
import com.cts.transport_gov.authentication_service.respository.CitizenDocumentRepository;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class CitizenDocumentServiceImpl implements ICitizenDocumentService {

    private final CitizenDocumentRepository citizenDocumentRepository;
    private final CitizenRepository citizenRepository;
    private final AuditLogRepository auditLogRepository;

    // ✅ FILE UPLOAD (KEEP THIS ONLY)
    @Override
    @Transactional
    public CitizenDocumentResponse uploadDocument(Long citizenId, String docType, MultipartFile file) {

        Citizen citizen = citizenRepository.findById(citizenId)
                .orElseThrow(() -> new RuntimeException("Citizen not found"));

        try {
            String uploadDir = "uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.write(filePath, file.getBytes());

            CitizenDocument document = CitizenDocument.builder()
                    .citizen(citizen)
                    .docType(docType)
                    .fileURI(filePath.toString())
                    .uploadedDate(LocalDate.now())
                    .verificationStatus(DocumentVerificationStatus.PENDING)
                    .build();

            CitizenDocument saved = citizenDocumentRepository.save(document);

            return mapToResponse(saved);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }

    // ✅ JSON Upload (keep as is)
    @Override
    @Transactional
    public String upload(DocumentUploadReq req) {

        log.info("Processing JSON document upload for citizenId={}", req != null ? req.getCitizenId() : "NULL");

        if (req == null || req.getCitizenId() == null) {
            throw new IllegalArgumentException("Request body or citizenId cannot be null");
        }

        Citizen citizen = citizenRepository.findById(req.getCitizenId())
                .orElseThrow(() -> new RuntimeException("Citizen not found"));

        CitizenDocument doc = CitizenDocument.builder()
                .citizen(citizen)
                .docType(req.getDocType())
                .fileURI(req.getFileUri())
                .uploadedDate(LocalDate.now())
                .verificationStatus(DocumentVerificationStatus.PENDING)
                .build();

        citizenDocumentRepository.save(doc);

        auditLogRepository.save(AuditLog.builder()
                .userId(req.getCitizenId())
                .action("DOCUMENT_UPLOAD_JSON")
                .resource("citizen_documents")
                .timestamp(LocalDateTime.now())
                .build());

        return "Document uploaded successfully for Citizen ID: " + req.getCitizenId();
    }

    // ✅ VERIFY
    @Override
    @Transactional
    public CitizenDocumentResponse verifyDocument(Long documentId, CitizenDocumentVerifyRequest request) {

        CitizenDocument document = citizenDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setVerificationStatus(request.getVerificationStatus());

        return mapToResponse(citizenDocumentRepository.save(document));
    }

    // ✅ GET ONE
    @Override
    public CitizenDocumentResponse getDocument(Long documentId) {
        return citizenDocumentRepository.findById(documentId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    // ✅ GET BY CITIZEN
    @Override
    public List<CitizenDocumentResponse> getDocumentsByCitizen(Long citizenId) {
        return citizenDocumentRepository.findByCitizen_CitizenId(citizenId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET ALL
    @Override
    public List<CitizenDocumentResponse> getAllCitizenDocuments() {
        return citizenDocumentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ MAPPER (ONLY ONE)
    private CitizenDocumentResponse mapToResponse(CitizenDocument doc) {
        CitizenDocumentResponse res = new CitizenDocumentResponse();
        res.setCitizenId(doc.getCitizen().getCitizenId());
        res.setDocType(doc.getDocType());
        res.setFileURI(doc.getFileURI());
        res.setUploadedDate(doc.getUploadedDate());
       res.setVerificationStatus(doc.getVerificationStatus());
        return res;
    }
}
