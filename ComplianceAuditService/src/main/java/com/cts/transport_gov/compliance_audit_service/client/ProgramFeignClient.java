package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.compliance_audit_service.dto.ProgramResponse;

import jakarta.validation.constraints.NotNull;

@FeignClient(name = "PROGRAMRESOURCESERVICE", path = "/programs")
public interface ProgramFeignClient {

	@GetMapping("/{programId}")
	public ResponseEntity<ProgramResponse> getProgram(
			@NotNull(message = "Program id should be provided") @PathVariable Long programId);
}
