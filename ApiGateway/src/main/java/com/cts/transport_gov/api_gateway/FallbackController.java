package com.cts.transport_gov.api_gateway;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "SERVICE_DOWN")
public class FallbackController {

	@GetMapping("/program-resource-service-fallback")
	public Mono<String> programResourceServiceCircuitBreaker() {
		return Mono.just("Program Service is currently unavailable");
	}

	@GetMapping("/ticket-fare-service-fallback")
	public Mono<String> ticketFareServiceCircuitBreaker() {
		return Mono.just("Ticketing Service is currently unavailable");
	}

	@GetMapping("/authentication-service-fallback")
	public Mono<String> authenticationServiceCircuitBreaker() {
		return Mono.just("Authentication Service is currently unavailable");
	}

	@GetMapping("/citizen-service-fallback")
	public Mono<String> citizenServiceCircuitBreaker() {
		return Mono.just("Citizen Service is currently unavailable");
	}

	@GetMapping("/compliance-audit-service-fallback")
	public Mono<String> complianceAuditServiceCircuitBreaker() {
		return Mono.just("Compliance and Audit Service is currently unavailable");
	}

	@RequestMapping(value = "/notification-service-fallback", method = { RequestMethod.GET, RequestMethod.POST,
			RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE })
	public Mono<String> notificationServiceCircuitBreaker() {
		return Mono.just("Notification Service is currently unavailable");
	}

	@GetMapping("/report-service-fallback")
	public Mono<String> reportAnalyticsServiceCircuitBreaker() {
		return Mono.just("Reporting Service is currently unavailable");
	}

	@GetMapping("/route-schedule-service-fallback")
	public Mono<String> routeScheduleServiceCircuitBreaker() {
		return Mono.just("Routes Service is currently unavailable");
	}

}
