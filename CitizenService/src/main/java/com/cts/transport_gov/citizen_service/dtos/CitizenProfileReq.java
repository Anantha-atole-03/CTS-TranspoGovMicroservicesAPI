package com.cts.transport_gov.citizen_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

/**
 * Data Transfer Object for Citizen Profile Request.
 * This class captures the necessary details to register or update a citizen's profile.
 */
@Data
@Slf4j
public class CitizenProfileReq {

    /** The full name of the citizen */
    private String name;

    /** The date of birth of the citizen */
    private LocalDate dob;

    /** The gender of the citizen (e.g., Male, Female, Other) */
    private String gender;

    /** The residential or permanent address of the citizen */
    private String address;

    /** Contact details such as phone number or email address */
    private String contactInfo;

    /**
     * Utility method to log the initialization or processing of the request.
     */
    public void logRequestDetails() {
        log.info("Processing CitizenProfileReq for: {}", name);
        log.debug("Full Request Data: {}", this.toString());
    }
}