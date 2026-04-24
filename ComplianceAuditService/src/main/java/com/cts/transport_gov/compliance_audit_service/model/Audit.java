package com.cts.transport_gov.compliance_audit_service.model;

import java.time.LocalDateTime;

import com.cts.transport_gov.compliance_audit_service.enums.AuditStatus;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
//@ToString(exclude = "findings")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Data
@Table(name = "audit")
public class Audit {

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Auditid")
	@EqualsAndHashCode.Include
	private Long id;

	@Column(name = "officer_id ", nullable = false)
	private Long officer_id;

	@Column(name = "Scope", nullable = false, length = 300)
	private String scope;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status", nullable = false, length = 20)
	@Builder.Default
	private AuditStatus status = AuditStatus.OPEN;

	@Column(name = "Date", nullable = false)
	@Builder.Default
	private LocalDateTime startedAt = LocalDateTime.now();

	@Column(name = "ClosedAt")
	private LocalDateTime closedAt;

//	@Column(name = "ReportUrl", length = 500)
//	private String reportUrl;

	private String findings;

}