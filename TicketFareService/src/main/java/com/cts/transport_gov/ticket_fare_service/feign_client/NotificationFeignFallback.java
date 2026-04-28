package com.cts.transport_gov.ticket_fare_service.feign_client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.ticket_fare_service.exceptions.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationFeignFallback implements NotificationFeignClient {

	@Override
	public ResponseEntity<?> sendBookNotification(Long id) {
		log.info("Inside notification feign Fallback method");
		throw new ServiceUnavailableException("Notification Service Unavailable. Try again later");
	}
}