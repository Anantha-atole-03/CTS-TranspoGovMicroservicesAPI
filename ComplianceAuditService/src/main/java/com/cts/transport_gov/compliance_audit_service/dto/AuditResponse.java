package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDateTime;

import com.cts.transport_gov.compliance_audit_service.enums.AuditStatus;

import lombok.Data;

@Data
public class AuditResponse {

	private Long id;
	private Long officer_id;
	private String scope;
	private AuditStatus status;
	private LocalDateTime startedAt;
	private LocalDateTime closedAt;
	private String findings;

}
