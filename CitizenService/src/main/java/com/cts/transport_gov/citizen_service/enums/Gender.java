package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * Enumeration representing the gender identity of a citizen.
 * Used for demographic tracking and official documentation.
 */
@Slf4j
public enum Gender {
	/** Biological male or self-identified male */
	MALE, 
	
	/** Biological female or self-identified female */
	FEMALE, 
	
	/** Non-binary or other gender identities */
	OTHER;

	/**
	 * Command method to log the gender selection being processed.
	 * Useful for auditing demographic data entry.
	 */
	public void logSelection() {
		log.info("Gender identity selected for processing: {}", this.name());
	}

	/**
	 * Command to perform a specific action based on the gender type.
	 */
	public void executeGenderSpecificTask() {
		log.debug("Executing specific business logic for gender: {}", this.toString());
		// Add logic here if specific processing is required per gender
	}
}