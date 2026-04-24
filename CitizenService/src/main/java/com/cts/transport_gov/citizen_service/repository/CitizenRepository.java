package com.cts.transport_gov.citizen_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.citizen_service.model.Citizen;


/**
 * Repository interface for {@link Citizen} entities.
 * Provides the abstraction layer for all database operations related to citizen profiles.
 */
@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    // Static logger for interface-level activity tracking
    Logger log = LoggerFactory.getLogger(CitizenRepository.class);

    /**
     * Command method to log database interaction attempts.
     * Useful for tracing repository access from the service layer.
     * @param action The description of the database action (e.g., "FETCH_BY_ID", "SAVE_CITIZEN")
     */
    default void logRepositoryCommand(String action) {
        log.info("CitizenRepository executing command: {}", action);
    }
}