package com.cts.transport_gov.authentication_service.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Signal the system that a requested user entity could not be located.
 * Logic: Extends RuntimeException to allow for unchecked exception handling within the Spring context.
 */
@Slf4j
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs the exception and logs the specific missing resource event.
     * @param message Descriptive error message containing the missing identifier.
     */
    public UserNotFoundException(String message) {
        super(message);
        log.error("UserNotFoundException raised: {}", message);
    }
}