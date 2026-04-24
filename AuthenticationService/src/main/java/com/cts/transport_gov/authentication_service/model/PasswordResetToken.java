package com.cts.transport_gov.authentication_service.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Securely store and manage time-limited tokens for the password recovery workflow.
 * Logic: Links a unique token string to a User ID and enforces security via an expiration timestamp.
 */
@Entity
@Data
@Slf4j
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    /**
     * Checks if the current token has passed its expiration window.
     * Logs a warning if an attempt is made to use an expired token.
     */
    public boolean isExpired() {
        boolean expired = LocalDateTime.now().isAfter(expiryDate);
        if (expired) {
            log.warn("Access attempt with expired token for User ID: {}. Expired at: {}", userId, expiryDate);
        }
        return expired;
    }
}