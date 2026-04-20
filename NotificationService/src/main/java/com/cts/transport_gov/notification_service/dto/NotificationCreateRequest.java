package com.cts.transport_gov.notification_service.dto;


import com.cts.transport_gov.notification_service.enums.NotificationCategory;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//@Data
public class NotificationCreateRequest {

	@NotNull(message = "User ID is required")
	private Long userId;

	private String entityId;

	@NotBlank(message = " Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Message cannot be empty")
	private String message;

	@NotNull(message = "Category is required")
	private NotificationCategory category;

	// Getters & Setters
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationCategory getCategory() {
		return category;
	}

	public void setCategory(NotificationCategory category) {
		this.category = category;
	}
}

