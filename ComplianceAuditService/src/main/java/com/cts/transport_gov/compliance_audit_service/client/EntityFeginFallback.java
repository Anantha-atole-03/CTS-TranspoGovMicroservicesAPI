package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.compliance_audit_service.dto.UserResponse;
import com.cts.transport_gov.compliance_audit_service.exceptions.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityFeginFallback implements EntityFeignClient {

	@Override

	public ResponseEntity<UserResponse> getuser(Long id) {
		log.info("Inside Citizen feign Fallback method");
		throw new ServiceUnavailableException("User Service Unavailable. Try again later");
	}
}
