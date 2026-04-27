package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDate;

import com.cts.transport_gov.compliance_audit_service.enums.ComplianceResultStatus;
import com.cts.transport_gov.compliance_audit_service.enums.ComplianceType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ComplianceCreateRequest {

	@Column(name = "Date", nullable = false)
	@JsonProperty("date")
	private LocalDate complianceDate;

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
