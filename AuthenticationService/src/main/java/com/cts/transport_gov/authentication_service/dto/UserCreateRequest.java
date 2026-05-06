package com.cts.transport_gov.authentication_service.dto;

import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.enums.UserStatus;

import lombok.Data;

@Data
public class UserCreateRequest {

	private String name;
	private UserRole role;
	private String email;
	private String phone;

	private String password;

	private UserStatus status;
}