package com.cts.transport_gov.program_resource_service.exception;

public class ProgramNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProgramNotFoundException(String msg) {
		super(msg);
	}

}
