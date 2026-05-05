package com.cts.transport_gov.compliance_audit_service.model;

import java.time.LocalDate;

import com.cts.transport_gov.compliance_audit_service.enums.ComplianceResultStatus;
import com.cts.transport_gov.compliance_audit_service.enums.ComplianceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "compliance_records")
@ToString
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL-friendly
	@Column(name = "ComplianceID", updatable = false, nullable = false)
	private Long complianceId;

	@Column(name = "Date", nullable = false)

	private LocalDate complianceDate;

	@Column(name = "EntityID", nullable = false)
	private Long entityId;

	@Column(name = "Notes", columnDefinition = "text")
	private String notes;

	@Enumerated(EnumType.STRING)
	@Column(name = "Result", nullable = false, length = 20)
	private ComplianceResultStatus result;

	@Enumerated(EnumType.STRING)
	@Column(name = "Type", nullable = false, length = 16)
	private ComplianceType type; // ROUTE / TICKET / PROGRAM

}