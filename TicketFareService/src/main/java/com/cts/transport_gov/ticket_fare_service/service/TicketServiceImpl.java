package com.cts.transport_gov.ticket_fare_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.ticket_fare_service.dto.CitizenResponse;
import com.cts.transport_gov.ticket_fare_service.dto.RouteResponse;
import com.cts.transport_gov.ticket_fare_service.dto.TicketCreateRequest;
import com.cts.transport_gov.ticket_fare_service.dto.TicketResponse;
import com.cts.transport_gov.ticket_fare_service.enums.PaymentMethod;
import com.cts.transport_gov.ticket_fare_service.enums.PaymentStatus;
import com.cts.transport_gov.ticket_fare_service.enums.TicketStatus;
import com.cts.transport_gov.ticket_fare_service.exceptions.CitizenNotFoundException;
import com.cts.transport_gov.ticket_fare_service.exceptions.TicketNotFoundException;
import com.cts.transport_gov.ticket_fare_service.exceptions.TicketStatusException;
import com.cts.transport_gov.ticket_fare_service.feign_client.CitizenFeignClient;
import com.cts.transport_gov.ticket_fare_service.feign_client.RouteFeignClient;
import com.cts.transport_gov.ticket_fare_service.model.Payment;
import com.cts.transport_gov.ticket_fare_service.model.Ticket;
import com.cts.transport_gov.ticket_fare_service.repository.IPaymentRepository;
import com.cts.transport_gov.ticket_fare_service.repository.ITicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for Ticket-related operations.
 *
 * @Service → Marks this as a Spring service component
 * @Slf4j → Enables SLF4J logging
 * @RequiredArgsConstructor → Constructor-based dependency injection
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements ITicketService {

	private final ModelMapper modelMapper;
	private final ITicketRepository ticketRepository;
	private final CitizenFeignClient citizenFeignClient;
	private final RouteFeignClient routeFeignClient;
	private final IPaymentRepository paymentRepository;

	/**
	 * Fetch all tickets booked by a specific citizen.
	 */
	@Override
	public List<TicketResponse> getMyAllTickets(Long citizenId) {

		log.info("Fetching all tickets for CitizenId: {}", citizenId);

		ResponseEntity<CitizenResponse> response = citizenFeignClient.getCitizenById(citizenId);
		if (!response.getStatusCode().equals(HttpStatus.OK)) {
			log.error("Citizen not found with id ticket service: {}", citizenId);
			throw new CitizenNotFoundException("Citizen account found with user");
		}

		// Fetch tickets and map to response DTOs
		return ticketRepository.findByCitizenOrderByDateDesc(response.getBody().getCitizenId()).stream().map(ticket -> {

			TicketResponse ticketBean = modelMapper.map(ticket, TicketResponse.class);
			ticketBean.setCitizenId(ticket.getCitizen());
			ticketBean.setRoute(modelMapper.map(ticket.getRoute(), RouteResponse.class));
			return ticketBean;
		}).collect(Collectors.toList());
	}

	/**
	 * Validates and confirms a ticket based on its current status.
	 */
	@Override

	public String checkTicket(Long ticketId) {

		log.info("Checking ticket status for TicketId: {}", ticketId);

		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
			log.error("Ticket not found with id: {}", ticketId);
			return new TicketNotFoundException("Invalid ticket id");
		});

		// Validate ticket status before confirmation
		if (ticket.getStatus().equals(TicketStatus.CANCELLED)) {
			log.warn("Ticket {} is CANCELLED", ticketId);
			throw new TicketStatusException("Ticket is cancelled");
		}

		if (ticket.getStatus().equals(TicketStatus.PENDING_PAYMENT)) {
			log.warn("Ticket {} payment pending", ticketId);
			throw new TicketStatusException("Ticket payment is pending, complete payment");
		}

		if (ticket.getStatus().equals(TicketStatus.EXPIRED)) {
			log.warn("Ticket {} is EXPIRED", ticketId);
			throw new TicketStatusException("Ticket is already expired");
		}

		// Update ticket status to CONFIRMED
		ticket.setStatus(TicketStatus.CONFIRMED);
		ticketRepository.save(ticket);

		log.info("Ticket {} confirmed successfully", ticketId);

		return "Ticket Confirmed";
	}

	/**
	 * Fetch a single ticket by its ID.
	 */
	@Override
	public TicketResponse getTicket(Long ticketId) {

		log.info("Fetching ticket details for TicketId: {}", ticketId);

		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
			log.error("Ticket not found with id: {}", ticketId);
			return new TicketNotFoundException("Invalid ticket");
		});
		TicketResponse response = modelMapper.map(ticket, TicketResponse.class);
		response.setCitizenId(ticket.getCitizen());
		response.setRoute(modelMapper.map(ticket.getRoute(), RouteResponse.class));

		return response;
	}

	/**
	 * Books a new ticket for a citizen. Initial ticket status is set to
	 * PENDING_PAYMENT.
	 */

	@Override
	public TicketResponse bookTicket(TicketCreateRequest ticketCreateRequest) {

		log.info("Booking ticket for CitizenId: {}", ticketCreateRequest.getCitizenId());

		// Validate citizen
		ResponseEntity<CitizenResponse> response = citizenFeignClient
				.getCitizenById(ticketCreateRequest.getCitizenId());
		log.info(response + " InTicketService");
		if (!response.getStatusCode().equals(HttpStatus.OK)) {
			log.error("Citizen not found with id ticket service: {}", ticketCreateRequest.getCitizenId());
			throw new CitizenNotFoundException("Citizen account found with user");
		}

		// validate route
		ResponseEntity<RouteResponse> apiResponse = routeFeignClient.getRouteById(ticketCreateRequest.getRouteId());
		if (!apiResponse.getStatusCode().equals(HttpStatus.OK)) {
			log.error("ROUTE not found with id in ticket service: {}", ticketCreateRequest.getRouteId());
			throw new RuntimeException("Route not found");
		}

		Ticket ticket = modelMapper.map(ticketCreateRequest, Ticket.class);
		log.warn(ticket.toString());
		ticket.setCitizen(response.getBody().getCitizenId());
		ticket.setRoute(apiResponse.getBody().getRouteId());
		ticket.setStatus(TicketStatus.PENDING_PAYMENT);

		Ticket savedTicket = ticketRepository.save(ticket);

		log.info("Ticket booked successfully. TicketId: {}", savedTicket.getTicketId());

		TicketResponse ticketResponse = modelMapper.map(savedTicket, TicketResponse.class);
		ticketResponse.setCitizenId(response.getBody().getCitizenId());

		return ticketResponse;
	}

	@Override
	public String cancelTicket(Long ticketId) {

		log.info("Cancel ticket request received for TicketId: {}", ticketId);

		// Fetch ticket or throw exception
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
			log.error("Ticket not found with id: {}", ticketId);
			return new TicketNotFoundException("Invalid ticket id");
		});

		// Validate current ticket status
		if (ticket.getStatus().equals(TicketStatus.CANCELLED)) {
			log.warn("Ticket {} is already cancelled", ticketId);
			throw new TicketStatusException("Ticket is already cancelled");
		}

		if (ticket.getStatus().equals(TicketStatus.EXPIRED)) {
			log.warn("Ticket {} is expired and cannot be cancelled", ticketId);
			throw new TicketStatusException("Expired ticket cannot be cancelled");
		}

		// Update status to CANCELLED
		ticket.setStatus(TicketStatus.CANCELLED);
		ticketRepository.save(ticket);

		log.info("Ticket {} cancelled successfully", ticketId);

		return "Ticket cancelled successfully";
	}

	@Override
	public String makePayment(Long ticketId, String paymentMethod) {

		log.info("Initiating payment for TicketId: {}", ticketId);

		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
			log.error("Ticket not found with id: {}", ticketId);
			return new TicketNotFoundException("Invalid ticket id");
		});

		if (!ticket.getStatus().equals(TicketStatus.PENDING_PAYMENT)) {
			log.warn("Payment not allowed for TicketId {} with status {}", ticketId, ticket.getStatus());
			throw new TicketStatusException("Payment not allowed for ticket status: " + ticket.getStatus());
		}

		Payment payment = Payment.builder().ticket(ticket).method(PaymentMethod.valueOf(paymentMethod.toUpperCase()))
				.date(LocalDateTime.now()).status(PaymentStatus.SUCCESS).build();

		paymentRepository.save(payment);

		ticket.setStatus(TicketStatus.CONFIRMED);
		ResponseEntity<CitizenResponse> response = citizenFeignClient.getCitizenById(ticket.getCitizen());
		if (!response.getStatusCode().equals(HttpStatus.OK)) {
			log.error("Citizen not found with id ticket service: {}", ticket.getCitizen());
			throw new CitizenNotFoundException("Citizen account found with user");
		}
		ticketRepository.save(ticket);

		log.info("Payment successful for TicketId: {}", ticketId);
//		if (ticket.getCitizen().getEmail() != null) {
//			String info = String.format(
//					"Title: %s <br> Route : %d <br> Start from: %s <br> End Stop: %s <br> Time: %s <br> <b>Payment Status:</b> %s <br>Payment successful <br> Transaction Id:%d <br> Amount:%f",
//					ticket.getRoute().getTitle(), ticket.getRoute(), ticket.getRoute().getStartPoint(),
//					ticket.getRoute().getEndPoint(), ticket.getDate().toString(), ticket.getStatus().toString(),
//					payment.getPaymentId(), ticket.getFareAmount());
//			notificationService.sendTicketNotification(response.getEmail(), info, ticket.getTicketId());
//		}

		return "Payment successful. Ticket confirmed.";
	}

	@Override
	public long countTickets() {
		return ticketRepository.count();
	}

}