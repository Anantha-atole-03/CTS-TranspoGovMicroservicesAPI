package com.cts.transport_gov.program_resource_service.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ResourceUtilizationRequest {
	private Long programId;
	private LocalDate utilizationDate;
	private Double quantityUsed;
	private Double costAmount;
	private String notes;
}
