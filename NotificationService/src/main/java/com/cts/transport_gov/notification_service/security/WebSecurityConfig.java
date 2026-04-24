package com.cts.transport_gov.notification_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cts.transport_gov.notification_service.enums.UserRole;

import lombok.RequiredArgsConstructor;

/**
 * Main Security Configuration class. Defines the security filter chain, path
 * permissions, and role-based access controls (RBAC) for the entire
 * application.
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	/**
	 * Configures the HTTP security, including CSRF, session management, and URL
	 * authorization.
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		final String CITIZEN_PASSENGER = UserRole.CITIZEN_PASSENGER.name();
		final String ADMINISTRATOR = UserRole.ADMINISTRATOR.name();
		final String TRANSPORT_OFFICER = UserRole.TRANSPORT_OFFICER.name();
		final String PROGRAM_MANAGER = UserRole.PROGRAM_MANAGER.name();
		final String COMPLIANCE_OFFICER = UserRole.COMPLIANCE_OFFICER.name();
		final String GOVERNMENT_AUDITOR = UserRole.GOVERNMENT_AUDITOR.name();

		http.csrf(csrf -> csrf.disable()) // Disabling CSRF as we use JWTs
				// Ensuring the application is stateless (no HttpSession used)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.disable()) // Disable default form-based login
				.httpBasic(basic -> basic.disable()) // Disable basic authentication

				.authorizeHttpRequests(auth -> auth

						// --- Public Endpoints ---
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						// --- Notifications ---
						.requestMatchers(HttpMethod.GET, "/notification").authenticated()
						.requestMatchers(HttpMethod.PATCH, "/notification/**").authenticated()
						.requestMatchers(HttpMethod.POST, "/notification/save")
						.hasAnyRole(TRANSPORT_OFFICER, PROGRAM_MANAGER, ADMINISTRATOR, COMPLIANCE_OFFICER)

						// All other requests must be authenticated
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * Exposes the AuthenticationManager as a Bean to be used in AuthService.
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
		return authenticationConfiguration.getAuthenticationManager();
	}
}