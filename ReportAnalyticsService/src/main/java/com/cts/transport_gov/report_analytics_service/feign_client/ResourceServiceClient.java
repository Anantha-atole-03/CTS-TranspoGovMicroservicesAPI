
package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.report_analytics_service.dto.ProgramUtilizationDTO;

@FeignClient(name = "ProgramResourceService", contextId = "resourceClient", // ✅ ADD THIS
		fallback = ResourceServiceFallback.class)
public interface ResourceServiceClient {

	@GetMapping("/resources/{programId}/utilizations")
	ProgramUtilizationDTO getProgramUtilization(@PathVariable("programId") Long programId);
}
