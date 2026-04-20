package com.cts.transport_gov.compliance_audit_service.dto;

import com.cts.transport_gov.compliance_audit_service.enums.ComplianceResultStatus;
import com.cts.transport_gov.compliance_audit_service.enums.ComplianceType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ComplianceCreateRequest {

	@NotNull(message = "EntityId can not be Null")
	@Positive(message = "EntityId should positive value")
	private Long entityId;
	@NotNull(message = "Notes can not be Null")
	private String notes;

	@NotNull(message = "ComplianceResultStatus can not be Null")
	private ComplianceResultStatus result;

	@NotNull(message = "result can not be Null")
	private ComplianceType type;

}
