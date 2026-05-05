package com.cts.transport_gov.notification_service.dto;

public class ComplianceNotificationRequest {

	private String email;
	private String entity;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
}