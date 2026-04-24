package com.cts.transport_gov.compliance_audit_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.compliance_audit_service.dto.RouteResponse;

@FeignClient(name = "ROUTESCHEDULESERVICE", path = "/route")
public interface RouteFeignClient {

	@GetMapping("/{id}")
	public ResponseEntity<RouteResponse> getRouteById(@PathVariable Long id);

	@GetMapping("/type/{type}")
	public ResponseEntity<List<RouteResponse>> getRoutesByType(@PathVariable String type);
}
