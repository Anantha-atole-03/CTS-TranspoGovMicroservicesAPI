package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDateTime;

import com.cts.transport_gov.compliance_audit_service.enums.AuditStatus;

import lombok.Data;

@Data
public class UpdateAuditResponse {

	private Long auditId;
	private String scope;
	private AuditStatus status;
	private String findings;
	private String message;
	private LocalDateTime updatedAt;

}