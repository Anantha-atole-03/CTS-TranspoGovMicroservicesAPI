package com.cts.transport_gov.ticket_fare_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.ticket_fare_service.dto.TicketCreateRequest;
import com.cts.transport_gov.ticket_fare_service.dto.TicketResponse;
import com.cts.transport_gov.ticket_fare_service.service.ITicketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for Ticket-related operations.
 * 
 * @RestController → Marks this class as REST API controller
 * @RequestMapping → Base URL mapping for ticket APIs
 * @Slf4j → Enables logging
 * @RequiredArgsConstructor → Constructor-based DI
 */
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

	private final ITicketService ticketService;

	/**
	 * Description: Get all tickets booked by a citizen. URL: GET
	 * /tickets/citizen/{citizenId}
	 */
	@GetMapping("/citizen/{citizenId}")
	public ResponseEntity<List<TicketResponse>> getMyAllTickets(@PathVariable Long citizenId) {

		log.info("API call: Get all tickets for CitizenId: {}", citizenId);

		List<TicketResponse> tickets = ticketService.getMyAllTickets(citizenId);

		return ResponseEntity.ok(tickets);
	}

	@GetMapping("/count")
	public ResponseEntity<Long> getTicketCount() {

		return ResponseEntity.ok(ticketService.countTickets());
	}

	/**
	 * Description: Get ticket details by ticket ID. URL: GET
	 * /api/tickets/{ticketId}
	 */
	@GetMapping("/{ticketId}")
	public ResponseEntity<TicketResponse> getTicket(@PathVariable Long ticketId) {

		log.info("API call: Get ticket details for TicketId: {}", ticketId);

		TicketResponse response = ticketService.getTicket(ticketId);

		return ResponseEntity.ok(response);
	}

	/**
	 * Description: Book a new ticket. URL: POST /tickets/book
	 */
	@PostMapping("/book")
	public ResponseEntity<TicketResponse> bookTicket(@RequestBody TicketCreateRequest request) {

		log.info("API call: Book ticket for CitizenId: {}", request.getCitizenId());
		TicketResponse response = ticketService.bookTicket(request);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * Description: Check and confirm ticket. URL: PUT /tickets/check/{ticketId}
	 */
	@PutMapping("/check/{ticketId}")
	public ResponseEntity<String> checkTicket(@PathVariable Long ticketId) {

		log.info("API call: Check ticket for TicketId: {}", ticketId);

		String message = ticketService.checkTicket(ticketId);

		return ResponseEntity.ok(message);
	}

	/**
	 * Description: Cancel a ticket. URL: PUT /tickets/cancel/{ticketId}
	 */
	@PutMapping("/cancel/{ticketId}")
	public ResponseEntity<String> cancelTicket(@PathVariable Long ticketId) {

		log.info("API call: Cancel ticket for TicketId: {}", ticketId);

		String message = ticketService.cancelTicket(ticketId);

		return ResponseEntity.ok(message);
	}

	/**
	 * Description: Make payment for a ticket. URL: PUT /tickets/cancel/{ticketId}
	 */
	@PostMapping("/{ticketId}/payment")
	public ResponseEntity<String> makePayment(@PathVariable Long ticketId, @RequestParam String paymentMethod) {

		return ResponseEntity.ok(ticketService.makePayment(ticketId, paymentMethod));
	}

}
