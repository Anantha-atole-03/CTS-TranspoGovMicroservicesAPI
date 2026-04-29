package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.compliance_audit_service.dto.UserResponse;

@FeignClient(name = "AUTHENTICATIONSERVICE", fallback = EntityFeginFallback.class, path = "/users")
public interface EntityFeignClient {

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getuser(@PathVariable Long id);
}