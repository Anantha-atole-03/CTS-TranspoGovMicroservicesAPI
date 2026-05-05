package com.cts.transport_gov.authentication_service.dto;

import java.time.LocalDate;

import com.cts.transport_gov.authentication_service.enums.DocumentVerificationStatus;

import lombok.Data;

@Data
public class CitizenDocumentResponse {
	private Long documentId;
	private Long citizenId;
	private String docType;
	private String fileURI;
	private LocalDate uploadedDate;
	private DocumentVerificationStatus verificationStatus;
}
