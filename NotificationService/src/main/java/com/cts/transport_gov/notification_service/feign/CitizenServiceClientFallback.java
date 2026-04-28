package com.cts.transport_gov.notification_service.feign;

import org.springframework.stereotype.Component;

import com.cts.transport_gov.notification_service.dto.CitizenResponse;
import com.cts.transport_gov.notification_service.exception.ServiceUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CitizenServiceClientFallback implements CitizenServiceClient {

	@Override
	public CitizenResponse getCitizen(Long id) {
		log.info("Inside CitizenServiceClient Feign Fallback method - getCitizen");
		throw new ServiceUnavailableException("Authentication Service Unavailable. Try again later");
	}

	@Override
	public CitizenResponse getCitizenByEmail(String email) {
		log.info("Inside CitizenServiceClient Feign Fallback method - getCitizenByEmail");
		throw new ServiceUnavailableException("Authentication Service Unavailable. Try again later");
	}
}