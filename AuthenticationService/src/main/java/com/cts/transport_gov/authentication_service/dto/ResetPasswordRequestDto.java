package com.cts.transport_gov.authentication_service.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
	private String email;
	private String newPassword;
}
