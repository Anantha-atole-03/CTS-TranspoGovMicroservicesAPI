package com.cts.transport_gov.ticket_fare_service.service;

import java.util.List;

import com.cts.transport_gov.ticket_fare_service.dto.TicketCreateRequest;
import com.cts.transport_gov.ticket_fare_service.dto.TicketResponse;

public interface ITicketService {
	List<TicketResponse> getMyAllTickets(Long citizenId);

	String checkTicket(Long ticketId);

	TicketResponse getTicket(Long ticketId);

	TicketResponse bookTicket(TicketCreateRequest ticketCreateRequest);

	String cancelTicket(Long ticketId);

	String makePayment(Long ticketId, String paymentMethod);

	long countTickets();
}
