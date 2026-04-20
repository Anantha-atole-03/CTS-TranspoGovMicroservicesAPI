package com.cts.transport_gov.ticket_fare_service.dto;

import java.time.LocalDateTime;

import com.cts.transport_gov.ticket_fare_service.enums.TicketStatus;

import lombok.Data;

@Data
public class TicketResponse {
	private Long ticketId;
	private Long citizenId;
	private RouteResponse route;
	private LocalDateTime date;
	private Double fareAmount;
	private TicketStatus status;
	private LocalDateTime createdAt;
}
