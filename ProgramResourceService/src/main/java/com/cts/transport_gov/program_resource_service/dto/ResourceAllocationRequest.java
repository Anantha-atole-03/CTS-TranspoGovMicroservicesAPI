package com.cts.transport_gov.program_resource_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceAllocationRequest {
	@NotNull(message = "Program id required")
	private Long programId;
	@NotNull(message = "Quantity required")
	private Double quantity;
	@NotNull(message = "Start date required")
	private LocalDate effectiveFrom;
	@NotNull(message = "End date required")
	private LocalDate effectiveTo;
	private String notes;
}
