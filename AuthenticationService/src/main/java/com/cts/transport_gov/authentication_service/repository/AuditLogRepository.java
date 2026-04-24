package com.cts.transport_gov.authentication_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.transport_gov.authentication_service.model.AuditLog;

/**
 * COMMAND: Interface for automated persistence and retrieval of security audit trails.
 * Logic: Extends JpaRepository to provide standard CRUD operations for the AuditLog entity.
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Interfaces cannot use Lombok @Slf4j directly, so we initialize the logger here
    Logger log = LoggerFactory.getLogger(AuditLogRepository.class);

    /**
     * Helper to log persistence events before saving.
     */
    default void logAndSave(AuditLog auditLog) {
        log.info("Persisting AuditLog entry: Action={} by UserID={}", 
                 auditLog.getAction(), auditLog.getUserId());
        save(auditLog);
    }
}