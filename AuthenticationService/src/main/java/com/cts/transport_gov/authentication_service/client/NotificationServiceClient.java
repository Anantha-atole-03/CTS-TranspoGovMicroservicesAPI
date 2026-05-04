package com.cts.transport_gov.authentication_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.transport_gov.authentication_service.dto.OtpNotificationRequest;

@FeignClient(name = "NOTIFICATIONSERVICE")
public interface NotificationServiceClient {

	@PostMapping("/notification/otp")
	public ResponseEntity<String> sendOtpNotification(@RequestBody OtpNotificationRequest request);

}
