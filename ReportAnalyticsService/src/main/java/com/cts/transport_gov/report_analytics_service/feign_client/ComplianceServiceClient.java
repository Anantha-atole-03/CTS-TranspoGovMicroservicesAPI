package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ComplianceAuditService", fallback = ComplianceServiceFallback.class)

public interface ComplianceServiceClient {

	@GetMapping("/compliance/summary")
	int getComplianceAlerts();

}
