package com.cts.transport_gov.citizen_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.citizen_service.model.AuditLog;

/**
 * Repository interface for {@link AuditLog} entities.
 * Provides standard CRUD operations and custom logging capabilities for technical auditing.
 * Satisfies requirement 4.1 for technical audit logging.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Static logger for interface-level logging
    Logger log = LoggerFactory.getLogger(AuditLogRepository.class);

    /**
     * Command method to log a summary of the current operation.
     * Useful for tracking repository-level activity in the logs.
     * * @param operation A description of the action being performed (e.g., "FETCH_BY_USER")
     */
    default void logRepositoryAction(String operation) {
        log.info("AuditLogRepository executing operation: {}", operation);
    }
}