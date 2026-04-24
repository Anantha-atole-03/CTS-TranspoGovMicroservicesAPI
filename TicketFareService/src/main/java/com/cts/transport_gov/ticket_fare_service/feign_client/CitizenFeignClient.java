package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.ticket_fare_service.dto.CitizenResponse;

@FeignClient(name = "AUTHENTICATIONSSERVICE", fallback = CitizenFeignFallback.class)
public interface CitizenFeignClient {
	@GetMapping("/citizen/{id}")
	ResponseEntity<CitizenResponse> getCitizenById(@PathVariable("id") Long id);
}
