package com.cts.transport_gov.citizen_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for representing a Citizen's profile information.
 * This class is used to send citizen details as a response to the client.
 */
@Data
@Slf4j
public class CitizenProfileResp {

    /** Unique identifier of the citizen */
    private Long id; 

    /** Full name of the citizen */
    private String name; 

    /** Current status (e.g., Active, Inactive, Pending) */
    private String status; 

    /** Residential or official address */
    private String address;      

    /** Contact details such as phone number or email */
    private String contactInfo; 

    /** Timestamp when the profile was initially created */
    private LocalDateTime createdDate; 

    /** Timestamp of the most recent update to the profile */
    private LocalDateTime lastModifiedDate; 

    /**
     * Logs the current state of the response object for debugging purposes.
     */
    public void logProfileDetails() {
        log.info("Accessing profile data for ID: {}", id);
        log.debug("Full profile details: {}", this.toString());
    }
}