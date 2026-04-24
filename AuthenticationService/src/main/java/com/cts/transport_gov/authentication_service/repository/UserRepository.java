package com.cts.transport_gov.authentication_service.repository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.transport_gov.authentication_service.model.User;

/**
 * COMMAND: Manage the persistence and unique identity verification of User entities.
 * Logic: Provides high-performance lookup methods for email-based authentication 
 * and data integrity checks for phone numbers.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Interfaces require manual logger initialization
    Logger log = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Retrieves a user by their unique email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a phone number is already registered in the system.
     */
    boolean existsByPhone(String phone);

    /**
     * COMMAND: Perform a logged lookup for a user to track authentication attempts.
     */
    default Optional<User> findByEmailAndLog(String email) {
        log.info("Querying database for user email: {}", email);
        Optional<User> user = findByEmail(email);
        
        if (user.isPresent()) {
            log.debug("User record located for email: {}", email);
        } else {
            log.warn("No user record found for email: {}", email);
        }
        
        return user;
    }
}