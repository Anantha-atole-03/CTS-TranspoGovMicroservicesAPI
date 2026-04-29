package com.cts.transport_gov.notification_service.feign;

import org.springframework.stereotype.Component;

import com.cts.transport_gov.notification_service.dto.UserDTO;
import com.cts.transport_gov.notification_service.exception.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserServiceClientFallback implements UserServiceClient {

	@Override
	public UserDTO getUserById(Long id) {
		log.info("Inside UserServiceClient Feign Fallback method - getUserById");
		throw new ServiceUnavailableException("Authentication Service Unavailable. Try again later");
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		log.info("Inside UserServiceClient Feign Fallback method - getUserByEmail");
		throw new ServiceUnavailableException("Authentication Service Unavailable. Try again later");
	}
}