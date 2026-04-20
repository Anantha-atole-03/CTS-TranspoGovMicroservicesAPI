package com.cts.transport_gov.compliance_audit_service.dto;

import com.cts.transport_gov.compliance_audit_service.enums.ComplianceResultStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ComplianceUpdate {
	@NotNull(message = "{Compliance.result.notNull}")
	private ComplianceResultStatus result;

	@Size(max = 1000, message = "{Compliance.notes.size")
	private String notes;
}
