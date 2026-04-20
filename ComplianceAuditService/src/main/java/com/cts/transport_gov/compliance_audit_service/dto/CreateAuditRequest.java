package com.cts.transport_gov.compliance_audit_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateAuditRequest {

	@NotNull(message = "officerId is required")
	@Positive(message = "officerId should be positive")
	private Long officerId;

	@NotBlank(message = "scope must not be blank")
	private String scope;
	@NotBlank(message = "finding must not be blank")
	private String findings;

}