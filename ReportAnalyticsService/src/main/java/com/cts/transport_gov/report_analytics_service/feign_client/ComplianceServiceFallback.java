package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.stereotype.Component;

import com.cts.transport_gov.report_analytics_service.exception.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ComplianceServiceFallback implements ComplianceServiceClient {

	@Override
	public int getComplianceAlerts() {
		log.info("Inside Compliance Service Feign Fallback method");
		throw new ServiceUnavailableException("Compliance Audit Service Unavailable. Try again later");
	}
}