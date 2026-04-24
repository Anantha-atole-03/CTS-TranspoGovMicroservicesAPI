package com.cts.transport_gov.citizen_service.repository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.citizen_service.model.CitizenDocument;

import java.util.List;

/**
 * Repository interface for {@link CitizenDocument} entities.
 * Handles database abstraction for citizen-uploaded documentation.
 */
@Repository
public interface CitizenDocumentRepository extends JpaRepository<CitizenDocument, Long> {

    // Static logger for interface-level activity tracking
    Logger log = LoggerFactory.getLogger(CitizenDocumentRepository.class);

    /**
     * Finds all documents associated with a specific citizen.
     * @param citizenId the unique identifier of the citizen.
     * @return a list of documents belonging to the citizen.
     */
    List<CitizenDocument> findByCitizenId(Long citizenId);

    /**
     * Command method to log search activity for specific citizen documents.
     * @param citizenId the ID being queried.
     * @param count the number of documents found.
     */
    default void logSearchActivity(Long citizenId, int count) {
        log.info("Query executed: Found {} documents for Citizen ID: {}", count, citizenId);
    }
}