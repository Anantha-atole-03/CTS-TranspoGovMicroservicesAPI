package com.cts.transport_gov.authentication_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.dto.CitizenDocumentCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenDocumentResponse;
import com.cts.transport_gov.authentication_service.dto.CitizenDocumentVerifyRequest;
import com.cts.transport_gov.authentication_service.enums.DocumentVerificationStatus;
import com.cts.transport_gov.authentication_service.model.Citizen;
import com.cts.transport_gov.authentication_service.model.CitizenDocument;
import com.cts.transport_gov.authentication_service.respository.CitizenDocumentRepository;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for managing citizen documents. Handles the logic for
 * uploading, retrieving, and verifying physical and digital documents.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CitizenDocumentServiceImpl implements ICitizenDocumentService {

	private final CitizenDocumentRepository citizenDocumentRepository;
	private final CitizenRepository citizenRepository;

	/**
	 * Links a file path to a citizen and persists document metadata. * @param
	 * request DTO containing citizen ID and document type.
	 * 
	 * @param filePath           The server-side path where the file is stored.
	 * @param verificationStatus The initial status (e.g., PENDING).
	 * @return Response DTO with the saved document details.
	 * @throws RuntimeException if the associated citizen does not exist.
	 */
	@Override
	public CitizenDocumentResponse uploadDocument(CitizenDocumentCreateRequest request, String filePath,
			String verificationStatus) {

		log.info("Uploading document for citizenId={}", request.getCitizenId());

		// Verify that the citizen exists before attaching a document
		Citizen citizen = citizenRepository.findById(request.getCitizenId())
				.orElseThrow(() -> new RuntimeException("Citizen not found"));

		// Build the entity using the provided file path and current system date
		CitizenDocument document = CitizenDocument.builder().citizen(citizen).docType(request.getDocType())
				.fileURI(filePath).uploadedDate(LocalDate.now())
				.verificationStatus(DocumentVerificationStatus.valueOf(verificationStatus)).build();

		CitizenDocument saved = citizenDocumentRepository.save(document);
		return mapToResponse(saved);
	}

	/**
	 * Updates the verification status of an existing document. * @param documentId
	 * The unique ID of the document.
	 * 
	 * @param request DTO containing the new status.
	 * @return Updated document details.
	 */
	@Override
	public CitizenDocumentResponse verifyDocument(Long documentId, CitizenDocumentVerifyRequest request) {

		CitizenDocument document = citizenDocumentRepository.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		// Update the status based on the reviewer's input
		document.setVerificationStatus(request.getVerificationStatus());

		CitizenDocument updated = citizenDocumentRepository.save(document);
		return mapToResponse(updated);
	}

	/**
	 * Fetches a single document's metadata by ID. * @param documentId The
	 * document's primary key.
	 * 
	 * @return Document response DTO.
	 */
	@Override
	public CitizenDocumentResponse getDocument(Long documentId) {
		CitizenDocument document = citizenDocumentRepository.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		return mapToResponse(document);
	}

	/**
	 * Retrieves all documents belonging to a specific citizen. * @param citizenId
	 * The ID of the citizen.
	 * 
	 * @return A list of document response objects.
	 */
	@Override
	public List<CitizenDocumentResponse> getDocumentsByCitizen(Long citizenId) {
		return citizenDocumentRepository.findByCitizen_CitizenId(citizenId).stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	/**
	 * Utility method to transform a CitizenDocument entity into a
	 * CitizenDocumentResponse DTO. * @param doc The source entity.
	 * 
	 * @return The target DTO.
	 */
	private CitizenDocumentResponse mapToResponse(CitizenDocument doc) {
		CitizenDocumentResponse res = new CitizenDocumentResponse();
		res.setDocumentId(doc.getDocumentId());
		res.setCitizenId(doc.getCitizen().getCitizenId());
		res.setDocType(doc.getDocType());
		res.setFileURI(doc.getFileURI());
		res.setUploadedDate(doc.getUploadedDate());
		res.setVerificationStatus(doc.getVerificationStatus());
		return res;
	}
}