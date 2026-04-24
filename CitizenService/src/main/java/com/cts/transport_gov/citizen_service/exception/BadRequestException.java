package com.cts.transport_gov.citizen_service.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception thrown when a request contains invalid data or violates business constraints.
 * Maps typically to an HTTP 400 Bad Request status.
 */
@Slf4j
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new BadRequestException with the specified detail message.
     * * @param message the detail message saved for later retrieval by the getMessage() method.
     */
    public BadRequestException(String message) {
        super(message);
        log.warn("BadRequestException initialized with message: {}", message);
    }

    /**
     * Utility method to log the exception context at an error level if needed.
     */
    public void logError() {
        log.error("Bad Request Error: {}", this.getMessage());
    }
}