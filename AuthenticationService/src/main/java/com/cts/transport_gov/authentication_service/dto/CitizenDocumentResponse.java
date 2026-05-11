package com.cts.transport_gov.authentication_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.cts.transport_gov.authentication_service.enums.DocumentVerificationStatus;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CitizenDocumentResponse {
    private Long citizenId;
    private String docType;
    private String fileURI;
    private LocalDate uploadedDate;
    private DocumentVerificationStatus verificationStatus;
}