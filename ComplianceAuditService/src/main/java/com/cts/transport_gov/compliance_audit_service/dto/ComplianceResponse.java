package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDate;

import com.cts.transport_gov.compliance_audit_service.enums.ComplianceResultStatus;
import com.cts.transport_gov.compliance_audit_service.enums.ComplianceType;

import lombok.Data;

@Data
public class ComplianceResponse {

	private Long complianceId;
	private LocalDate complianceDate;
	private Long entityId;
//	private ComplianceEntityResponseDto entityData;
	private String notes;
	private ComplianceResultStatus result;
	private ComplianceType type;

}
