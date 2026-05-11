package com.cts.transport_gov.report_analytics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramUtilizationDTO {

	private Long programId;

	private String title;
	private String description;

	private double allocatedBudget; // ✅ must match
	private double utilizedBudget; // ✅ must match
	private double remainingBudget; // ✅ must match

	private double budgetUtilizationPercentage; // ✅ must match

	private int totalResourcesAllocated; // ✅ must match
	private int totalResourcesUsed; // ✅ must match
}
