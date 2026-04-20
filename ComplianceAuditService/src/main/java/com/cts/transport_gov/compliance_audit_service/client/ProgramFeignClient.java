package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.cts.transport_gov.compliance_audit_service.dto.ApiResponse;
import com.cts.transport_gov.compliance_audit_service.dto.ProgramResponse;

@FeignClient(name = "program-service")
public interface ProgramFeignClient {

	@GetMapping("/{programId}")
	ResponseEntity<ApiResponse<ProgramResponse>> getProgramById(Long ref);

}
