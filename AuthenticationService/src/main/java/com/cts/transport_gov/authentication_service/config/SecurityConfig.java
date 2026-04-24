package com.cts.transport_gov.authentication_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Establish the application's global security perimeter and authorization matrix.
 * Logic: Configures stateless JWT authentication, disables CSRF for API compatibility, 
 * and maps granular role-based access control (RBAC) to specific HTTP endpoints.
 */
@Configuration
@EnableWebSecurity 
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * COMMAND: Define the security filter chain and request matching rules.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Initializing Security Filter Chain with Stateless Session Policy...");

        return http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // 1. PUBLIC & AUTHENTICATION
                .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login", "/auth/forgot-password", "/auth/reset-password").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/validate").permitAll()
                .requestMatchers(HttpMethod.GET, "/programs/{programId}").permitAll()
                .requestMatchers("/error", "/h2-console/**").permitAll()

                // 2. TICKETING & FARE COLLECTION
                .requestMatchers(HttpMethod.GET, "/tickets").hasAuthority("CITIZEN")
                .requestMatchers(HttpMethod.POST, "/tickets/{ticketId}/check").hasAuthority("TRANSPORT_OFFICER")
                .requestMatchers(HttpMethod.GET, "/tickets/{ticketId}").hasAnyAuthority("CITIZEN", "TRANSPORT_OFFICER", "COMPLIANCE_OFFICER")

                // 3. TRANSPORT PROGRAMS & RESOURCES
                .requestMatchers(HttpMethod.GET, "/programs").hasAnyAuthority("CITIZEN", "TRANSPORT_OFFICER", "PROGRAM_MANAGER", "ADMINISTRATOR", "COMPLIANCE_OFFICER")
                .requestMatchers(HttpMethod.POST, "/programs/**").hasAuthority("PROGRAM_MANAGER")
                .requestMatchers(HttpMethod.POST, "/programs/{programId}/approve").hasAnyAuthority("PROGRAM_MANAGER", "ADMINISTRATOR", "COMPLIANCE_OFFICER")
                .requestMatchers(HttpMethod.GET, "/resources").hasAnyAuthority("PROGRAM_MANAGER", "ADMINISTRATOR", "COMPLIANCE_OFFICER")
                .requestMatchers(HttpMethod.POST, "/resources/**").hasAnyAuthority("PROGRAM_MANAGER", "COMPLIANCE_OFFICER")

                // 4. COMPLIANCE MODULE
                .requestMatchers(HttpMethod.POST, "/compliance/save").hasAuthority("COMPLIANCE_OFFICER")
                .requestMatchers(HttpMethod.GET, "/compliance/summary").hasAnyAuthority("COMPLIANCE_OFFICER", "PROGRAM_MANAGER", "ADMINISTRATOR")
                .requestMatchers(HttpMethod.PUT, "/compliance/update/**").hasAuthority("COMPLIANCE_OFFICER")
                .requestMatchers("/compliance/**").hasAnyAuthority("COMPLIANCE_OFFICER", "ADMINISTRATOR", "FINANCIAL_OFFICER")

                // 5. AUDIT MODULE
                .requestMatchers(HttpMethod.POST, "/audit/save_audits").hasAuthority("GOVERNMENT_AUDITOR")
                .requestMatchers(HttpMethod.GET, "/audit/summary").hasAnyAuthority("GOVERNMENT_AUDITOR", "PROGRAM_MANAGER", "ADMINISTRATOR")
                .requestMatchers("/audit/**").hasAnyAuthority("GOVERNMENT_AUDITOR", "ADMINISTRATOR")

                // 6. REPORTING & NOTIFICATIONS
                .requestMatchers("/reports/**").hasAnyAuthority("PROGRAM_MANAGER", "ADMINISTRATOR", "COMPLIANCE_OFFICER", "GOVERNMENT_AUDITOR")
                .requestMatchers(HttpMethod.POST, "/notifications/save").hasAnyAuthority("TRANSPORT_OFFICER", "PROGRAM_MANAGER", "ADMINISTRATOR", "COMPLIANCE_OFFICER")
                
                // 7. GLOBAL FALLBACK
                .anyRequest().authenticated())
            
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    
    /**
     * COMMAND: Provide BCrypt hashing for secure password storage.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Configuring BCrypt password encoder bean.");
        return new BCryptPasswordEncoder();
    }

    /**
     * COMMAND: Expose the AuthenticationManager for use in the login service.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.debug("Exposing AuthenticationManager bean from Configuration.");
        return config.getAuthenticationManager();
    }
}