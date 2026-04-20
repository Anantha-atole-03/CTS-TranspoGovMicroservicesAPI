package com.cts.transport_gov.ticket_fare_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCreateRequest {
	@NotNull(message = "Ticket id required")
	private Long ticketId;
	@NotNull(message = "Payment method required")
	private String method; // Cash/Card/Wallet
}
