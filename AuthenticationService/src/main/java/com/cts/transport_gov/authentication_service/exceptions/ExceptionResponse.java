package com.cts.transport_gov.authentication_service.exceptions;

import java.time.LocalDateTime;

public class ExceptionResponse {
	private String message;
	private LocalDateTime time;
	private int stateCode;

	public ExceptionResponse() {
		super();
	}

	public ExceptionResponse(String message, int stateCode) {
		this.message = message;
		this.time = LocalDateTime.now();
		this.stateCode = stateCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

}
