
package com.cts.transport_gov.citizen_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cts.transport_gov.citizen_service.enums.GenericStatus;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity representing a Citizen within the JusticeGov system.
 * Handles the persistence of personal details, contact information, and account status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Slf4j
@Table(name = "Citizen")
public class Citizen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CitizenID")
	private Long id;

	/** Legal full name of the citizen */
	@Column(name = "Name", nullable = false)
	private String name;

	/** Date of birth used for age verification and identification */
	@Column(name = "DOB", nullable = false)
	private LocalDate dob;

	/** Gender identification */
	@Column(name = "Gender")
	private String gender;

	/** Residential or permanent address */
	@Column(name = "Address")
	private String address;

	/** Primary contact method (Phone/Email) - must be unique */
	@Column(name = "ContactInfo", nullable = false, unique = true)
	private String contactInfo;

	/** Current lifecycle status of the citizen record */
	@Enumerated(EnumType.STRING)
	@Column(name = "Status")
	private GenericStatus status;

	/** Timestamp when the record was first persisted */
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	/** Timestamp of the last update to the record */
	@Column(name = "last_modified_date")
	private LocalDateTime lastModifiedDate;

	/**
	 * Lifecycle hook to set default values and timestamps before persisting.
	 */
	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		if (this.createdDate == null) {
			this.createdDate = now;
		}
		this.lastModifiedDate = now;
		if (this.status == null) {
			this.status = GenericStatus.PENDING;
		}
		log.info("Preparing to persist new Citizen: {} with status: {}", name, status);
	}

	/**
	 * Lifecycle hook to refresh the last modified timestamp before an update.
	 */
	@PreUpdate
	protected void onUpdate() {
		this.lastModifiedDate = LocalDateTime.now();
		log.debug("Updating Citizen ID: {}. Last modified set to: {}", id, lastModifiedDate);
	}

	/**
	 * Command to log the current state of the Citizen entity.
	 */
	public void logCitizenDetails() {
		log.info("Citizen Summary [ID: {}, Name: {}, Status: {}]", id, name, status);
	}
}