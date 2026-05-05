package com.cts.transport_gov.compliance_audit_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.transport_gov.compliance_audit_service.dto.ComplianceNotificationRequest;

@FeignClient(name = "NOTIFICATIONSERVICE", path = "/notification", fallback = NotificationFeignFallback.class)
public interface NotificationFeignClient {

	@PostMapping("/compliance")
	ResponseEntity<String> sendComplianceNotification(@RequestBody ComplianceNotificationRequest request);
}
