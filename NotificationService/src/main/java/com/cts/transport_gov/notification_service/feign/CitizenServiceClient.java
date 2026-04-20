package com.cts.transport_gov.notification_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.notification_service.dto.CitizenDTO;

@FeignClient(name = "CitizenService")
public interface CitizenServiceClient {

	@GetMapping("/citizens/{id}")
	CitizenDTO getCitizenById(@PathVariable Long id);

	@GetMapping("/citizens/email/{email}")
	CitizenDTO getCitizenByEmail(@PathVariable String email);

}
