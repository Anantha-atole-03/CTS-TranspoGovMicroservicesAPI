package com.cts.transport_gov.notification_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a citizen is not found in CitizenService.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CitizenNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CitizenNotFoundException(String message) {
		super(message);
	}
}
