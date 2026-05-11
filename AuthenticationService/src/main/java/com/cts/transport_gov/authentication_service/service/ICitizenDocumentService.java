package com.cts.transport_gov.authentication_service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cts.transport_gov.authentication_service.dto.CitizenDocumentResponse;
import com.cts.transport_gov.authentication_service.dto.CitizenDocumentVerifyRequest;
import com.cts.transport_gov.authentication_service.dto.DocumentUploadReq;

public interface ICitizenDocumentService {
	CitizenDocumentResponse verifyDocument(Long documentId, CitizenDocumentVerifyRequest request);

	CitizenDocumentResponse getDocument(Long documentId);

	List<CitizenDocumentResponse> getDocumentsByCitizen(Long citizenId);

	// CitizenDocumentResponse uploadDocument(CitizenDocumentCreateRequest request,
	// String filePath,
	//String verificationStatus);
	

	String upload(DocumentUploadReq req);

	List<CitizenDocumentResponse> getAllCitizenDocuments();
	CitizenDocumentResponse uploadDocument(Long citizenId, String docType, MultipartFile file);
}