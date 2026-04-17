package com.cts.transport_gov.program_resource_service.dto;

import com.cts.transport_gov.program_resource_service.enums.ResourceStatus;
import com.cts.transport_gov.program_resource_service.enums.ResourceType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ResourceCreateRequest {
	@NotNull(message = "Program id required")

	private Long programId;
	@NotNull(message = "Resource type should be provided")

	private ResourceType type;
	@NotNull(message = "Quntity should be provided")
	@Positive(message = "Quantity should be positive number")
	private int quantity;
	private ResourceStatus status;
	@NotNull(message = "Budget should be provided")
	@Positive(message = "Budget should be positive number")
	private double budget;
}
