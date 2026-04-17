package com.cts.transport_gov.route_schedule_service.exceptions;

// Custom exception thrown when a Schedule is not found.
 
public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
