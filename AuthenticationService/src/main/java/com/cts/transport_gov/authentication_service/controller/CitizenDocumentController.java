package com.cts.transport_gov.authentication_service.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.cts.transport_gov.authentication_service.dto.*;
import com.cts.transport_gov.authentication_service.service.ICitizenDocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citizen-documents")
@RequiredArgsConstructor
@EnableMethodSecurity
public class CitizenDocumentController {

	private final ICitizenDocumentService citizenDocumentService;

	// ===================== MULTIPART FILE UPLOAD =====================

	@PostMapping("/upload")
	@PreAuthorize("hasRole('CITIZEN_PASSENGER')")
	public ResponseEntity<CitizenDocumentResponse> uploadDocument(@RequestParam Long citizenId,
			@RequestParam String docType, @RequestParam MultipartFile file) {

		CitizenDocumentResponse response = citizenDocumentService.uploadDocument(citizenId, docType, file);
		return ResponseEntity.ok(response);

	}

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

	@GetMapping("/admin/citizens-documents/{adminId}")
	public List<CitizenDocumentResponse> getAllCitizenDocs() {

		return citizenDocumentService.getAllCitizenDocuments();
	}

	@GetMapping("/citizen/{citizenId}")
	public List<CitizenDocumentResponse> getByCitizen(@PathVariable Long citizenId) {
		return citizenDocumentService.getDocumentsByCitizen(citizenId);
	}

	// ===================== VERIFY =====================

	@PutMapping("/verify/{documentId}")
	public CitizenDocumentResponse verify(@PathVariable Long documentId,
			@RequestBody CitizenDocumentVerifyRequest request) {
		return citizenDocumentService.verifyDocument(documentId, request);
	}
}