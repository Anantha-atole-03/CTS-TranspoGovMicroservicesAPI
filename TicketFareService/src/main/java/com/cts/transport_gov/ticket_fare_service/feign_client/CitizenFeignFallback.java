package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.ticket_fare_service.dto.CitizenResponse;
import com.cts.transport_gov.ticket_fare_service.exceptions.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CitizenFeignFallback implements CitizenFeignClient {

	@Override
	public ResponseEntity<CitizenResponse> getCitizenById(Long id) {
		log.info("Inside Citizen feign Fallback method");
		throw new ServiceUnavailableException("User Service Unavailable. Try again later");
	}
}