package com.cts.transport_gov.authentication_service.dto;

import com.cts.transport_gov.authentication_service.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
	private String phone;
	private UserRole role;
	private String token;
}
