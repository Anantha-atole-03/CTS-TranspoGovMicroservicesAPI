package com.cts.transport_gov.program_resource_service.dto;

import java.time.LocalDate;

import com.cts.transport_gov.program_resource_service.enums.ProgramStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProgramCreateRequest {
	@NotBlank(message = "Please provide title")
	private String title;
	@NotBlank(message = "Please provide description")
	private String description;
	@FutureOrPresent(message = "End Date should be future date")
	@NotNull(message = "Start Date Should be provided")
	private LocalDate startDate;
	@Future(message = "End Date should be future date")
	@NotNull(message = "End Date Should be provided")
	private LocalDate endDate;
	@NotNull(message = "Budget Should be provided")
	@Positive(message = "Bugdet Should be positive")
	@Min(1000)
	private Double budget;
	@NotNull(message = "Status should be provided")
	private ProgramStatus status;
}
