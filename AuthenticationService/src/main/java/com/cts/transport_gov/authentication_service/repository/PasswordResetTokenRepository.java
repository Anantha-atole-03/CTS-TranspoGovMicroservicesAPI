package com.cts.transport_gov.authentication_service.repository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.transport_gov.authentication_service.model.PasswordResetToken;

/**
 * COMMAND: Provide data access for security tokens used in password recovery.
 * Logic: Facilitates the lookup of reset tokens to verify authenticity and expiration before allowing credential changes.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Manual logger initialization as @Slf4j is not applicable to interfaces
    Logger log = LoggerFactory.getLogger(PasswordResetTokenRepository.class);

    /**
     * Finds a token in the database.
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * COMMAND: Retrieve and log the status of a token lookup attempt.
     */
    default Optional<PasswordResetToken> findAndLogToken(String token) {
        log.info("Attempting to locate reset token in database...");
        Optional<PasswordResetToken> result = findByToken(token);
        
        if (result.isPresent()) {
            log.info("Token found for User ID: {}", result.get().getUserId());
        } else {
            log.warn("Token lookup failed: Token does not exist.");
        }
        
        return result;
    }
}