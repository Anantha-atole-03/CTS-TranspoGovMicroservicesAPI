package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.ticket_fare_service.dto.CitizenResponse;

@Component
public class CitizenFeignFallback implements CitizenFeignClient {

	@Override
	public ResponseEntity<CitizenResponse> getCitizenById(Long id) {
		CitizenResponse citizenResponse = new CitizenResponse();
		citizenResponse.setCitizenId(0l);
		citizenResponse.setName("UNKNOWN");

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(citizenResponse);
	}
}