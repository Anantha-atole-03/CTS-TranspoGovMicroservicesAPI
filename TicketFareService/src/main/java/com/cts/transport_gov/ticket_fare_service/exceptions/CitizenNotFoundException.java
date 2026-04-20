package com.cts.transport_gov.ticket_fare_service.exceptions;

public class CitizenNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CitizenNotFoundException(String message) {
		super(message);
	}

}
