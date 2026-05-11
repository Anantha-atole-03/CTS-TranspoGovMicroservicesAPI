package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.stereotype.Component;

import com.cts.transport_gov.report_analytics_service.dto.ProgramUtilizationDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ResourceServiceFallback implements ResourceServiceClient {

	@Override
	public ProgramUtilizationDTO getProgramUtilization(Long programId) {

		log.warn("⚠️ FALLBACK triggered - returning zero data for programId={}", programId);

		return ProgramUtilizationDTO.builder().programId(programId).title("N/A").allocatedBudget(0.0)
				.utilizedBudget(0.0).remainingBudget(0.0).budgetUtilizationPercentage(0.0).totalResourcesAllocated(0)
				.totalResourcesUsed(0).build();
	}
}