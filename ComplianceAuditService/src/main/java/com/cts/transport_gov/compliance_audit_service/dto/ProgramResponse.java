package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDate;

import com.cts.transport_gov.compliance_audit_service.enums.ProgramStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramResponse {
	private Long programId;
	private String title;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double budget;
	private ProgramStatus status;
}
