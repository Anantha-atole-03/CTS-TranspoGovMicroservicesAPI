package com.cts.transport_gov.notification_service.exception;



public class NotificationNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotificationNotFoundException(String msg) {
		super(msg);
	}
}
