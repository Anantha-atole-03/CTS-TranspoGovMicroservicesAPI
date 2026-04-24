package com.cts.transport_gov.authentication_service.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
	private String phone;
	private String password;
}
