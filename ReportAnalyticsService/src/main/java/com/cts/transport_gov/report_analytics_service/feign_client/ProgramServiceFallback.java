package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProgramServiceFallback implements ProgramServiceClient {

	@Override
	public double calculateEfficiency() {
		log.warn("Program Resource Service is down -> returning fallback value");
		return 0.0;
	}
}
