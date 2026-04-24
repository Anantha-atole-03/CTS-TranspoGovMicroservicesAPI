package com.cts.transport_gov.citizen_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for standardized Error Responses.
 * This class is used to communicate exception details across different modules of the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class ErrorResponse {

    /** The exact time the error occurred */
    private LocalDateTime timestamp;

    /** A descriptive message explaining the error */
    private String message;

    /** The specific application module where the error originated */
    private String module;  

    /** The HTTP status code associated with the error */
    private int status;

    /**
     * Logs the error details for server-side auditing and debugging.
     */
    public void logErrorDetails() {
        log.error("Error occurred in module [{}]: {} (Status: {})", module, message, status);
        log.debug("Full error context: {}", this.toString());
    }
}