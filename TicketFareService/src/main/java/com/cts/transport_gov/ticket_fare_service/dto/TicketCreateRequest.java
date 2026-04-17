package com.cts.transport_gov.ticket_fare_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TicketCreateRequest {
	private Long citizenId;
	private Long routeId;
	private LocalDateTime date;
	private Double fareAmount;
//  private TicketStatus status; 
}
