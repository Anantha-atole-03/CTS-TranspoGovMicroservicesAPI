package com.cts.transport_gov.report_analytics_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FullAnalyticsResponse {

	// CORE METRICS
	private int activeRoutes;
	private long totalTickets;
	private int complianceAlerts;

	// PROGRAM + RESOURCE
	private ProgramUtilizationDTO programUtilization;

	// ADVANCED CALCULATED METRICS
	private double ticketPerRoute;
	private double resourceEfficiency;
	private double budgetEfficiency;
}
