package com.cts.transport_gov.ticket_fare_service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TicketCreateRequest {
	private Long citizenId;
	@NotNull(message = "Route required")
	private Long routeId;
	@NotNull(message = "Date and Time required")
	private LocalDateTime date;
	@NotNull(message = "Amount required")
	@Positive(message = "Amount should positive")
	private Double fareAmount;
//  private TicketStatus status; 
}
