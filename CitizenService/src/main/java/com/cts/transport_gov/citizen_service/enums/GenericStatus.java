package com.cts.transport_gov.citizen_service.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * A universal status enum used across the JusticeGov system to track 
 * the state of various entities like Citizens, Documents, and Requests.
 */
@Slf4j
public enum GenericStatus {
	/** Entity is fully operational and verified */
	ACTIVE,

	/** Entity is disabled or no longer in use */
	INACTIVE, 

	/** Entity is awaiting review or processing */
	PENDING, 

	/** Entity has been officially verified and accepted */
	APPROVED, 

	/** Entity has been dismissed or found invalid */
	REJECTED, 

	/** Entity is restricted due to security or compliance violations */
	BLOCKED;

	/**
	 * Command method to log the transition or current state of an entity.
	 * Helps in tracing workflow progress in the service logs.
	 */
	public void logStatusChange() {
		log.info("Status state accessed: {}", this.name());
	}

	/**
	 * Command to log a warning if the status represents a restricted state.
	 */
	public void logIfRestricted() {
		if (this == REJECTED || this == BLOCKED) {
			log.warn("Access attempt on a restricted status: {}", this.name());
		}
	}
}