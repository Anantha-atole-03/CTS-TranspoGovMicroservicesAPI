package com.cts.transport_gov.report_analytics_service.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardResponse {
    private int activeRoutes;
    private long totalTickets;
    private int complianceAlerts;
    private double programEfficiency;
}