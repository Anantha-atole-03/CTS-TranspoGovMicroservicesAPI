package com.cts.transport_gov.authentication_service.dto;

import java.time.LocalDate;

import com.cts.transport_gov.authentication_service.enums.CitizenStatus;

import lombok.Data;

@Data
public class CitizenCreateRequest {
	private String name;
	private LocalDate dob;
	private String gender;
	private String address;
	private String phone;
	private String password;
	private CitizenStatus status;
}
