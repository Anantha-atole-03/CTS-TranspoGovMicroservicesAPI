package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.compliance_audit_service.dto.ComplianceNotificationRequest;
import com.cts.transport_gov.compliance_audit_service.exceptions.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationFeignFallback implements NotificationFeignClient {

	@Override
	public ResponseEntity<String> sendComplianceNotification(ComplianceNotificationRequest request) {

		log.error("Notification Service is DOWN");
		throw new ServiceUnavailableException("Notification Service Unavailable. Try again later");
	}
}