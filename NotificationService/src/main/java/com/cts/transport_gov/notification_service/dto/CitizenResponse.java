package com.cts.transport_gov.notification_service.dto;

import java.time.LocalDate;

import com.cts.transport_gov.notification_service.enums.CitizenStatus;

import lombok.Data;

@Data
public class CitizenResponse {
	private Long citizenId;
	private String name;
	private LocalDate dob;
	private String gender;
	private String address;
	private String phone;
	private CitizenStatus status;
}