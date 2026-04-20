package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.ticket_fare_service.dto.RouteResponse;

@FeignClient(name = "ROUTESCHEDULESERVICE")
public interface RouteFeignClient {
	@GetMapping("/route/{id}")
	ResponseEntity<RouteResponse> getRouteById(@PathVariable("id") Long id);
}
