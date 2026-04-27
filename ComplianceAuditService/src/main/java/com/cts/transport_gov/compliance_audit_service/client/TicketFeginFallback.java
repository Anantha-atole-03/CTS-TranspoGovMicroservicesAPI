package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.http.ResponseEntity;

import com.cts.transport_gov.compliance_audit_service.dto.TicketResponse;
import com.cts.transport_gov.compliance_audit_service.exceptions.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TicketFeginFallback implements TicketFeginCient {

	@Override
	public ResponseEntity<TicketResponse> getTicket(Long ticketId) {
		log.info("Inside Ticket feign Fallback method");
		throw new ServiceUnavailableException("User Service Unavailable. Try again later");

	}

}
