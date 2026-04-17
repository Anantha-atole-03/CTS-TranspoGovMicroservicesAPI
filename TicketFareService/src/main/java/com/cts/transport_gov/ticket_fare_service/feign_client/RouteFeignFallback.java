package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RouteFeignFallback implements RouteFeignClient {

	@Override
	public ResponseEntity getRouteById(Long id) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Route Service in unavailable try again later");
	}
}