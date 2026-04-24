package com.cts.transport_gov.citizen_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a specific Judiciary or Research resource cannot be found.
 * This class ensures that a 404 NOT FOUND status is returned to the client.
 * * @author JusticeGov Developer
 */
@Slf4j
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with a specific detail message.
     * * @param message the detail message describing the missing resource.
     */
    public ResourceNotFoundException(String message) {
        super(message);
        log.error("ResourceNotFoundException triggered: {}", message);
    }

    /**
     * Command method to log additional context or stack trace details for debugging.
     */
    public void logDetailedError() {
        log.debug("Stack trace for missing resource: ", this);
    }
}