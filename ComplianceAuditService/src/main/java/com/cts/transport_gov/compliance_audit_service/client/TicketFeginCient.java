package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.compliance_audit_service.dto.TicketResponse;

@FeignClient(name = "TICKETFARESERVICE", path = "/ticket")
public interface TicketFeginCient {

	@GetMapping("/{ticketId}")
	public ResponseEntity<TicketResponse> getTicket(@PathVariable Long ticketId);

}
