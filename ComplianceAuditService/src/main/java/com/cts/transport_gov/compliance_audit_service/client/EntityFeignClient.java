package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.compliance_audit_service.dto.UserResponse;

@FeignClient(name = "user-service")
public interface EntityFeignClient {

	@GetMapping("/api/users/{id}")
	ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id);
}