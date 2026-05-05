package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.ticket_fare_service.dto.RouteResponse;
import com.cts.transport_gov.ticket_fare_service.exceptions.GlobalExceptionHandler;
import com.cts.transport_gov.ticket_fare_service.exceptions.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RouteFeignFallback implements RouteFeignClient {

	private final GlobalExceptionHandler globalExceptionHandler;

	RouteFeignFallback(GlobalExceptionHandler globalExceptionHandler) {
		this.globalExceptionHandler = globalExceptionHandler;
	}

	@Override
	public ResponseEntity<RouteResponse> getRouteById(Long id) {
		log.info("Inside Route feign Fallback method");
		throw new ServiceUnavailableException("Route Service Unavailable. Try again later");
	}
}