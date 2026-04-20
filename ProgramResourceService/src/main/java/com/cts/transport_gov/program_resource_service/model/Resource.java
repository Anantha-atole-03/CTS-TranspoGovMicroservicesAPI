package com.cts.transport_gov.program_resource_service.model;



import com.cts.transport_gov.program_resource_service.enums.ResourceStatus;
import com.cts.transport_gov.program_resource_service.enums.ResourceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "resource_id", updatable = false, nullable = false)
	private Long resourceId;

	@NotNull(message = "Program details required")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "program_id", nullable = false)
	private TransportProgram program;

	@NotNull(message = "Resource type required")
	@Enumerated(EnumType.STRING)
	private ResourceType type;
	@Positive(message = "Resource quantity should be positive")
	@NotNull(message = "Resource type required")
	private int quantity;
	private double budget;

	@NotNull(message = "Resource status required")
	@Enumerated(EnumType.STRING)
	private ResourceStatus status;
}