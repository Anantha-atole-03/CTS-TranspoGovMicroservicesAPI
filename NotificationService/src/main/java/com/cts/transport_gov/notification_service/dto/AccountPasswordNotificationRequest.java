
package com.cts.transport_gov.notification_service.dto;

import lombok.Data;

@Data
public class AccountPasswordNotificationRequest {

	private String email;
	private String name;
	private String phone;
	private String password;
	private boolean isCitizen;
}
