package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TicketServiceFallback implements TicketServiceClient {

	@Override
	public long countTickets() {
		log.warn("Ticket Service is down -> returning fallback value");
		return 0L;
	}
}