package com.cts.transport_gov.compliance_audit_service.dto;

import com.cts.transport_gov.compliance_audit_service.enums.UserRole;
import com.cts.transport_gov.compliance_audit_service.enums.UserStatus;

import lombok.Data;

@Data
public class UserResponse {
	private Long userId;
	private String name;
	private UserRole role;
	private String email;
	private String phone;
	private UserStatus status;
}