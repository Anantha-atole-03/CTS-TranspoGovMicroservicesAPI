package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.stereotype.Component;

import com.cts.transport_gov.report_analytics_service.exception.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RouteServiceFallback implements RouteServiceClient {

	@Override
	public int countActiveRoutes() {
		log.info("Inside Route Service Feign Fallback method");
		throw new ServiceUnavailableException("Route Schedule Service Unavailable. Try again later");
	}
}
