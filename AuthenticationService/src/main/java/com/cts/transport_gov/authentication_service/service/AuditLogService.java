package com.cts.transport_gov.authentication_service.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.respository.AuditLogRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(Long userId, String action, String resource) {
        AuditLog logEntry = new AuditLog();
        logEntry.setUserId(userId);
        logEntry.setAction(action);
        logEntry.setResource(resource);
        logEntry.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(logEntry);
    }

    // ✅ OVERLOADED METHOD
    public void logAction(User user, String action, String resource) {
        logAction(user.getUserId(), action, resource);
    }
}