package com.cts.transport_gov.authentication_service.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.transport_gov.authentication_service.dto.CitizenDocumentCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenDocumentResponse;
import com.cts.transport_gov.authentication_service.dto.CitizenDocumentVerifyRequest;
import com.cts.transport_gov.authentication_service.service.ICitizenDocumentService;

import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing citizen document uploads and verification.
 * Provides endpoints for document storage, retrieval, and status updates.
 */
@RestController
@RequestMapping("/api/citizen-documents")
@RequiredArgsConstructor
public class CitizenDocumentController {

	private final ICitizenDocumentService citizenDocumentService;

	/**
	 * Handles the physical file upload and persists metadata in the database.
	 * * @param citizenId ID of the citizen owning the document.
	 * 
	 * @param file               The multipart file object from the request.
	 * @param docType            The category/type of document (e.g., PASSPORT,
	 *                           LICENSE).
	 * @param verificationStatus Initial status (defaults to PENDING).
	 * @return CitizenDocumentResponse containing the saved document details.
	 * @throws IOException If there is an error saving the file to the local
	 *                     directory.
	 */
	@PostMapping(value = "/upload/{citizenId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CitizenDocumentResponse uploadDocument(@PathVariable Long citizenId,
			@RequestParam("file") MultipartFile file, @RequestParam("docType") String docType,
			@RequestParam(value = "verificationStatus", required = false, defaultValue = "PENDING") String verificationStatus)
			throws IOException {

		// Ensure the local upload directory exists
		String uploadDir = "uploads";
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Generate a unique file name to avoid collisions
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(uploadDir, fileName);

		// Write the file to the server's file system
		Files.write(filePath, file.getBytes());

		// Prepare the request DTO for the service layer
		CitizenDocumentCreateRequest request = new CitizenDocumentCreateRequest();
		request.setCitizenId(citizenId);
		request.setDocType(docType);

		return citizenDocumentService.uploadDocument(request, filePath.toString(), verificationStatus);
	}

	/**
	 * Retrieves metadata for a specific document by its ID. * @param documentId
	 * Unique identifier of the document.
	 * 
	 * @return Document details.
	 */
	@GetMapping("/{documentId}")
	public CitizenDocumentResponse getDocument(@PathVariable Long documentId) {
		return citizenDocumentService.getDocument(documentId);
	}

	/**
	 * Retrieves all documents associated with a specific citizen. * @param
	 * citizenId ID of the citizen.
	 * 
	 * @return List of document responses.
	 */
	@GetMapping("/citizen/{citizenId}")
	public List<CitizenDocumentResponse> getDocumentsByCitizen(@PathVariable Long citizenId) {
		return citizenDocumentService.getDocumentsByCitizen(citizenId);
	}

	/**
	 * Updates the verification status of a document (e.g., APPROVED, REJECTED).
	 * * @param documentId ID of the document to verify.
	 * 
	 * @param request DTO containing new status and optional remarks.
	 * @return Updated document details.
	 */
	@PutMapping("/verify/{documentId}")
	public CitizenDocumentResponse verifyDocument(@PathVariable Long documentId,
			@RequestBody CitizenDocumentVerifyRequest request) {
		return citizenDocumentService.verifyDocument(documentId, request);
	}
}