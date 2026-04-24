package com.cts.transport_gov.citizen_service.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception thrown when a requested resource (e.g., a Citizen or Document) 
 * cannot be found in the system.
 * This typically results in an HTTP 404 Not Found response.
 */
@Slf4j
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new NotFoundException with a specific error message.
     * @param message The detail message explaining what resource was not found.
     */
    public NotFoundException(String message) {
        super(message);
        log.error("NotFoundException raised: {}", message);
    }

    /**
     * Utility method to log the exception at a debug level for troubleshooting.
     */
    public void logExceptionDetails() {
        log.debug("Resource missing context: {}", this.getMessage());
    }
}