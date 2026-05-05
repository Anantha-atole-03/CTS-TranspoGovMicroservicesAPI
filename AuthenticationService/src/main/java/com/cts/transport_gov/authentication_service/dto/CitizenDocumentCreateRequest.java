package com.cts.transport_gov.authentication_service.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CitizenDocumentCreateRequest {
	private Long citizenId;
	private String docType;
	private String fileURI;
	
}
