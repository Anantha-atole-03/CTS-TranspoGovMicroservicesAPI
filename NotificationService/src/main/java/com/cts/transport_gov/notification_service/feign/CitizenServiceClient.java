package com.cts.transport_gov.notification_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.notification_service.dto.CitizenResponse;

@FeignClient(name = "AUTHENTICATIONSERVICE")
public interface CitizenServiceClient {

	@GetMapping("/citizen/{id}")
	CitizenResponse getCitizen(@PathVariable("id") Long id);

	@GetMapping("/citizen/email/{email}")
	CitizenResponse getCitizenByEmail(@PathVariable("email") String email);
}
