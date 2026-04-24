package com.cts.transport_gov.authentication_service.model;

import java.time.LocalDate;

import com.cts.transport_gov.authentication_service.enums.DocumentVerificationStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "citizen_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenDocument {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "document_id", updatable = false, nullable = false)
	private Long documentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "citizen_id", nullable = false)
	private Citizen citizen;

	private String docType;
	private String fileURI;
	private LocalDate uploadedDate;

	@Enumerated(EnumType.STRING)
	private DocumentVerificationStatus verificationStatus;
}