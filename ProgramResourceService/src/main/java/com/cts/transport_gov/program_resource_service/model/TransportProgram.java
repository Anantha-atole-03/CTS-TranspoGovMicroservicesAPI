package com.cts.transport_gov.program_resource_service.model;

import java.time.LocalDate;
import java.util.List;

import com.cts.transport_gov.program_resource_service.enums.ProgramStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transport_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportProgram {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "program_id", updatable = false, nullable = false)
	private Long programId;

	@NotBlank(message = "Please provide title")
	private String title;
	@NotBlank(message = "Please provide description")
	@Column(columnDefinition = "text")
	private String description;

	@NotNull(message = "Start Date Should be provided")
	private LocalDate startDate;
	@NotNull(message = "End Date Should be provided")
	private LocalDate endDate;

	@NotNull(message = "Budget Should be provided")
	@Positive(message = "Bugdet Should be positive")
	@Min(1000)
	private Double budget;

	@Enumerated(EnumType.STRING)
	private ProgramStatus status;

	@OneToMany(mappedBy = "program", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Resource> resources;

}
