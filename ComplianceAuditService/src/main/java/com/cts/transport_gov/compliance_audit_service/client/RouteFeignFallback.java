package com.cts.transport_gov.compliance_audit_service.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.compliance_audit_service.dto.RouteResponse;
import com.cts.transport_gov.compliance_audit_service.exceptions.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RouteFeignFallback implements RouteFeignClient {

	@Override
	public ResponseEntity<RouteResponse> getRouteById(Long id) {
		log.info("Inside Route feign Fallback method");
		throw new ServiceUnavailableException("Route Service Unavailable. Try again later");

	}

	@Override
	public ResponseEntity<List<RouteResponse>> getRoutesByType(String type) {
		log.info("Inside Route feign Fallback method");
		throw new ServiceUnavailableException("Route Service Unavailable. Try again later");

	}

}
