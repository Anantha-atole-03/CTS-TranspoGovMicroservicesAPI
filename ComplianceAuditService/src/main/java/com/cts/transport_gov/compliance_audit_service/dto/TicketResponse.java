package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDateTime;

import com.cts.transport_gov.compliance_audit_service.enums.TicketStatus;

import lombok.Data;

@Data
public class TicketResponse implements ComplianceEntityResponseDto {
	private Long ticketId;
	private Long citizenId;
	private RouteResponse route;
	private LocalDateTime date;
	private Double fareAmount;
	private TicketStatus status;
	private LocalDateTime createdAt;
}
