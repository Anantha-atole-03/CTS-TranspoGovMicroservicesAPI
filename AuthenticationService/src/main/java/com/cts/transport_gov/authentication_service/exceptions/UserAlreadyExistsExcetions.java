package com.cts.transport_gov.authentication_service.exceptions;

public class UserAlreadyExistsExcetions extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsExcetions(String msg) {
		super(msg);
	}
}
