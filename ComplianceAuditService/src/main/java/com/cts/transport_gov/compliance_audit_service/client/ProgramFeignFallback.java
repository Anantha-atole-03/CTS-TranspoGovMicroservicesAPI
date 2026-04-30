package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.compliance_audit_service.dto.ProgramResponse;
import com.cts.transport_gov.compliance_audit_service.exceptions.ServiceUnavailableException;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProgramFeignFallback implements ProgramFeignClient {

	@Override
	public ResponseEntity<ProgramResponse> getProgram(
			@NotNull(message = "Program id should be provided") Long programId) {
		log.info("Inside Program feign Fallback method");
		throw new ServiceUnavailableException("program Service Unavailable. Try again later");

	}
}
