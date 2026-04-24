package com.cts.transport_gov.authentication_service.config;



import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.repository.UserRepository;

import java.util.Collections;

/**
 * COMMAND: Bridge the application's user database with Spring Security's authentication engine.
 * Logic: Locates a user entity by email and transforms it into a UserDetails principal 
 * containing credentials and granted authorities (roles).
 */
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * COMMAND: Load a user's security context based on their email address.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Security context lookup initiated for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Security lookup failed: No account associated with {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        log.debug("User found. Mapping roles for security principal: {}", user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}