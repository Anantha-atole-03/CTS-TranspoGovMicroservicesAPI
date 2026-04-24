package com.cts.transport_gov.citizen_service.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.transport_gov.citizen_service.dtos.CitizenProfileReq;
import com.cts.transport_gov.citizen_service.dtos.CitizenProfileResp;
import com.cts.transport_gov.citizen_service.dtos.ProfileUpdateReq;

import java.util.List;

/**
 * Service interface for Citizen management.
 * Handles business logic for registration and profile maintenance.
 */
public interface CitizenService {

    // Static logger for service-level activity tracking
    Logger log = LoggerFactory.getLogger(CitizenService.class);

    /**
     * Registers citizens
     * @param req The registration request payload
     * @return The created citizen profile response
     */
    CitizenProfileResp register(CitizenProfileReq req);
    
    /**
     * Maintains profiles - Retrieves all citizen records
     * @return List of all citizen profiles
     */
    List<CitizenProfileResp> getAll();

    /**
     * Retrieves a specific citizen profile by ID.
     * @param id The unique identifier of the citizen
     * @return The found citizen profile
     */
    CitizenProfileResp getById(Long id);

    /**
     * Updates an existing citizen profile
     * @param id The ID of the citizen to update
     * @param req The updated data payload
     * @return The updated profile response
     */
    CitizenProfileResp updateProfile(Long id, ProfileUpdateReq req);

    /**
     * Command method to log the start of a service operation.
     * @param action The name of the operation being performed
     * @param identifier An optional ID or info string for context
     */
    default void logServiceAction(String action, String identifier) {
        log.info("CitizenService executing {}: [{}]", action, identifier);
    }

    /**
     * Command to log a successful completion of a business process.
     */
    default void logSuccess(String action) {
        log.debug("CitizenService successfully completed operation: {}", action);
    }
}