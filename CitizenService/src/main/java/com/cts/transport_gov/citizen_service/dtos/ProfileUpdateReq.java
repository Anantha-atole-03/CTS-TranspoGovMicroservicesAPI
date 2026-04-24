package com.cts.transport_gov.citizen_service.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object for Profile Update Requests.
 * Used to capture modified citizen details for existing profile records.
 */
@Data
@Slf4j
public class ProfileUpdateReq {

    /** Updated residential or official address */
    private String address;

    /** Updated contact information such as phone or email */
    private String contactInfo;

    /** Updated status of the profile (e.g., ACTIVE, SUSPENDED) */
    private String status;

    /**
     * Logs the update request details for auditing and tracing purposes.
     */
    public void logUpdateActivity() {
        log.info("Received profile update request with status: {}", status);
        log.debug("Update payload details: {}", this.toString());
    }
}