package com.cts.transport_gov.ticket_fare_service.dto;

import java.time.LocalDateTime;

import com.cts.transport_gov.ticket_fare_service.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {
	private Long paymentId;
	private Long ticketId;
	private String method;
	private LocalDateTime date;
	private PaymentStatus status;
	private LocalDateTime createdAt;
}
