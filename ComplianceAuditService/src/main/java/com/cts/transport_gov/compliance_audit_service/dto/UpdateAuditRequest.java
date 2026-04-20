package com.cts.transport_gov.compliance_audit_service.dto;

import com.cts.transport_gov.compliance_audit_service.enums.AuditStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAuditRequest {

	@NotBlank(message = "Scope is required")
	private String scope;

	@NotNull(message = "Status is required")
	private AuditStatus status;

	@Size(max = 100)
	@NotBlank(message = "Findings is required")
	private String findings;
}