package com.cts.transport_gov.authentication_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenDocumentRequest {
    private Long citizenId;
    private String docType;
    private MultipartFile file;
}
