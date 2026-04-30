package com.cts.transport_gov.compliance_audit_service.dto;

import com.cts.transport_gov.compliance_audit_service.enums.RouteStatus;

import lombok.Data;

@Data
public class RouteResponse implements ComplianceEntityResponseDto {
	private Long routeId;
	private String title;
	private String type;
	private String startPoint;
	private String endPoint;
	private RouteStatus status;
}
