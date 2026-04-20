package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.ticket_fare_service.dto.RouteResponse;
import com.cts.transport_gov.ticket_fare_service.exceptions.ServiceUnavailableException;

@Component
public class RouteFeignFallback implements RouteFeignClient {

	@Override
	public ResponseEntity<RouteResponse> getRouteById(Long id) {
		throw new ServiceUnavailableException("Route Service Unavailable. Try again later");
	}
}