package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATIONSERVICE", fallback = NotificationFeignFallback.class)
public interface NotificationFeignClient {
	@PostMapping("/notification/ticket")
	ResponseEntity<?> sendBookNotification(@RequestBody Long id);
}
