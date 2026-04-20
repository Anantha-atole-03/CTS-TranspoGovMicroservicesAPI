package com.cts.transport_gov.ticket_fare_service.dto;

import com.cts.transport_gov.ticket_fare_service.enums.RouteStatus;

import lombok.Data;

@Data
public class RouteResponse {
	private Long routeId;
	private String title;
	private String type;
	private String startPoint;
	private String endPoint;
	private RouteStatus status;
}
