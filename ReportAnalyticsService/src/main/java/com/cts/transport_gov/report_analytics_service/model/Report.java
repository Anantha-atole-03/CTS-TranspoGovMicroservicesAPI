package com.cts.transport_gov.report_analytics_service.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reports")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "report_id", updatable = false, nullable = false)
	private Long reportId;

	@NotBlank(message = "Scope is required (e.g.,Route, Ticket, or Program)")
	@Column(nullable = false, length = 50)
	private String scope;

	@Column(columnDefinition = "text")
	private String metrics;

	@Column(name = "program_id")
	private Long programId;

	@CreationTimestamp
	@Column(name = "generated_date", updatable = false)
	private LocalDateTime generatedDate;
}
