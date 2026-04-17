package com.cts.transport_gov.program_resource_service.dto;

import com.cts.transport_gov.program_resource_service.enums.ResourceStatus;
import com.cts.transport_gov.program_resource_service.enums.ResourceType;

import lombok.Data;

@Data
public class ResourceResponse {
	private Long resourceId;
	private Long programId;
	private ResourceType type;
	private int quantity;
	private ResourceStatus status;
	private double budget;
}
