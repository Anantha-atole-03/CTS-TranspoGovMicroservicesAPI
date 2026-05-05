package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.stereotype.Component;

import com.cts.transport_gov.report_analytics_service.exception.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TicketServiceFallback implements TicketServiceClient {

	@Override
	public long countTickets() {
		log.info("Inside Ticket Service Feign Fallback method");
		throw new ServiceUnavailableException("Ticket Fare Service Unavailable. Try again later");
	}
}