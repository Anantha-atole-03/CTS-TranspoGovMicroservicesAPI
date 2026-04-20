package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {

	private String message;
	private LocalDateTime time;
	private int statusCode; // renamed from stateCode
	private T data;

	public ApiResponse() {
		this.time = LocalDateTime.now();
	}

	public ApiResponse(String message, int statusCode) {
		this.message = message;
		this.statusCode = statusCode;
		this.time = LocalDateTime.now();
	}

	public ApiResponse(String message, int statusCode, T data) {
		this.message = message;
		this.statusCode = statusCode;
		this.data = data;
		this.time = LocalDateTime.now();
	}

	// Getters & Setters
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

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
