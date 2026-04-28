package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.stereotype.Component;

import com.cts.transport_gov.report_analytics_service.exception.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProgramServiceFallback implements ProgramServiceClient {

	@Override
	public double calculateEfficiency() {
		log.info("Inside Program Service Feign Fallback method");
		throw new ServiceUnavailableException("Program Resource Service Unavailable. Try again later");
	}
}