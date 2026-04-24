package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines the specific categories of compliance checks within the JusticeGov system.
 * Used to distinguish between different legal and administrative workflows.
 */
@Slf4j
public enum ComplianceType {
	/** Compliance related to legal cases */
	CASE, 
	
	/** Compliance related to scheduled court hearings */
	HEARING, 
	
	/** Compliance related to final judicial rulings */
	JUDGEMENT, 
	
	/** Compliance related to legal or policy research tasks */
	RESEARCH;

	/**
	 * Command method to log the type of compliance currently being processed.
	 * This helps in tracing which workflow branch the application has entered.
	 */
	public void logType() {
		log.info("Processing compliance workflow for type: {}", this.name());
	}

	/**
	 * Example command to provide a descriptive log for auditing.
	 */
	public void logAuditTrail() {
		log.debug("Audit Trail: Compliance type '{}' accessed for verification.", this.toString());
	}
}