package com.cts.transport_gov.notification_service.dto;

public class RouteNotificationRequest {

	private String email;
	private String route;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
}
