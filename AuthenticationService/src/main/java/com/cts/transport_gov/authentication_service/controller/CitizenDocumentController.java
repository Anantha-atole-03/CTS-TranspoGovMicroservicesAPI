package com.cts.transport_gov.authentication_service.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.cts.transport_gov.authentication_service.dto.*;
import com.cts.transport_gov.authentication_service.service.ICitizenDocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citizen-documents")
@RequiredArgsConstructor
public class CitizenDocumentController {

    private final ICitizenDocumentService citizenDocumentService;

    // ===================== MULTIPART FILE UPLOAD =====================

//    @PostMapping(
//        value = "/upload/{citizenId}",
//        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public CitizenDocumentResponse uploadDocument(
//            @PathVariable Long citizenId,
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("docType") String docType,
//            @RequestParam(
//                value = "verificationStatus",
//                defaultValue = "PENDING") String verificationStatus)
//            throws IOException {
//
//        String uploadDir = "uploads";
//        File dir = new File(uploadDir);
//        if (!dir.exists()) dir.mkdirs();
//
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        Path filePath = Paths.get(uploadDir, fileName);
//
//        Files.write(filePath, file.getBytes());
//
//        CitizenDocumentCreateRequest request = new CitizenDocumentCreateRequest();
//        request.setCitizenId(citizenId);
//        request.setDocType(docType);
//
//        return citizenDocumentService
//                .uploadDocument(request, filePath.toString(), verificationStatus);
//    }

    // ===================== JSON-ONLY (FRD) UPLOAD =====================

    @PostMapping("/upload-json")
    public String uploadViaJson(@RequestBody DocumentUploadReq request) {
        return citizenDocumentService.upload(request);
    }

    // ===================== GET =====================

    @GetMapping("/{documentId}")
    public CitizenDocumentResponse getDocument(@PathVariable Long documentId) {
        return citizenDocumentService.getDocument(documentId);
    }

    @GetMapping("/citizen/{citizenId}")
    public List<CitizenDocumentResponse> getByCitizen(
            @PathVariable Long citizenId) {
        return citizenDocumentService.getDocumentsByCitizen(citizenId);
    }

    // ===================== VERIFY =====================

    @PutMapping("/verify/{documentId}")
    public CitizenDocumentResponse verify(
            @PathVariable Long documentId,
            @RequestBody CitizenDocumentVerifyRequest request) {
        return citizenDocumentService.verifyDocument(documentId, request);
    }
}