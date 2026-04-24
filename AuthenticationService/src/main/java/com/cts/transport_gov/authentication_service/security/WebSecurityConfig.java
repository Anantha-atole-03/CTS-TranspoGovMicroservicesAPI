package com.cts.transport_gov.authentication_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cts.transport_gov.authentication_service.enums.UserRole;

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
	private final JwtAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;

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

				// Customizing error handling for 401 (Unauthorized) and 403 (Forbidden)
				.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler)
						.authenticationEntryPoint(authenticationEntryPoint))

				.authorizeHttpRequests(auth -> auth

						// --- Public Endpoints ---
						.requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/api/audit-logs/**")
						.permitAll()

						// --- Admin & Audit Specific ---
						.requestMatchers(HttpMethod.PUT, "/api/citizens/{userId}/role").hasRole(ADMINISTRATOR)
						.requestMatchers("/api/audit-logs/**").hasAnyRole(ADMINISTRATOR, GOVERNMENT_AUDITOR)

						// --- Citizen Management Endpoints ---
						.requestMatchers(HttpMethod.POST, "/citizen").permitAll() // Allow registration
						.requestMatchers(HttpMethod.GET, "/citizen")
						.hasAnyRole(ADMINISTRATOR, TRANSPORT_OFFICER, COMPLIANCE_OFFICER)
						.requestMatchers(HttpMethod.GET, "/citizen/{id}").hasAnyRole(ADMINISTRATOR, CITIZEN_PASSENGER)
						.requestMatchers(HttpMethod.PUT, "/citizen/{id}").hasAnyRole(ADMINISTRATOR, CITIZEN_PASSENGER)

						// --- Document Management ---
						.requestMatchers(HttpMethod.POST, "/api/citizen-documents/upload/**").hasRole(CITIZEN_PASSENGER)
						.requestMatchers(HttpMethod.GET, "/api/citizen-documents/citizen/**")
						.hasAnyRole(CITIZEN_PASSENGER, TRANSPORT_OFFICER, ADMINISTRATOR)
						.requestMatchers(HttpMethod.PUT, "/api/citizen-documents/verify/**")
						.hasAnyRole(TRANSPORT_OFFICER, COMPLIANCE_OFFICER)

						// --- Programs and Ticketing ---

						.requestMatchers(HttpMethod.GET, "/tickets/**").hasAnyRole(CITIZEN_PASSENGER, TRANSPORT_OFFICER)
						.requestMatchers(HttpMethod.POST, "/tickets/**").hasRole(CITIZEN_PASSENGER)
						.requestMatchers(HttpMethod.POST, "/tickets/*/check").hasRole(TRANSPORT_OFFICER)
						.requestMatchers(HttpMethod.GET, "/programs/{programId}").permitAll()
						.requestMatchers(HttpMethod.GET, "/programs")
						.hasAnyRole(CITIZEN_PASSENGER, TRANSPORT_OFFICER, PROGRAM_MANAGER, ADMINISTRATOR,
								COMPLIANCE_OFFICER)
						.requestMatchers(HttpMethod.POST, "/programs").hasRole(PROGRAM_MANAGER)
						.requestMatchers(HttpMethod.POST, "/programs/*/submit").hasRole(PROGRAM_MANAGER)
						.requestMatchers(HttpMethod.POST, "/programs/*/approve")
						.hasAnyRole(PROGRAM_MANAGER, ADMINISTRATOR)

						// --- Resource Allocation ---
						.requestMatchers(HttpMethod.GET, "/resources")
						.hasAnyRole(PROGRAM_MANAGER, ADMINISTRATOR, COMPLIANCE_OFFICER)
						.requestMatchers(HttpMethod.POST, "/resources/*/allocate")
						.hasAnyRole(PROGRAM_MANAGER, COMPLIANCE_OFFICER)
						.requestMatchers(HttpMethod.POST, "/resources/*/utilizations")
						.hasAnyRole(PROGRAM_MANAGER, COMPLIANCE_OFFICER)

						// --- Reporting ---
						.requestMatchers(HttpMethod.GET, "/report/operations")
						.hasAnyRole(PROGRAM_MANAGER, ADMINISTRATOR, COMPLIANCE_OFFICER, GOVERNMENT_AUDITOR)
						.requestMatchers(HttpMethod.POST, "/report/custom/run")
						.hasAnyRole(PROGRAM_MANAGER, ADMINISTRATOR, COMPLIANCE_OFFICER, GOVERNMENT_AUDITOR)
						.requestMatchers(HttpMethod.GET, "/report/custom/jobs/**")
						.hasAnyRole(PROGRAM_MANAGER, ADMINISTRATOR, COMPLIANCE_OFFICER, GOVERNMENT_AUDITOR)

						// --- Notifications ---
						.requestMatchers(HttpMethod.GET, "/notification").authenticated()
						.requestMatchers(HttpMethod.PATCH, "/notification/**").authenticated()
						.requestMatchers(HttpMethod.POST, "/notification/save")
						.hasAnyRole(TRANSPORT_OFFICER, PROGRAM_MANAGER, ADMINISTRATOR, COMPLIANCE_OFFICER)

						// --- User Management ---
						.requestMatchers("/user/**").hasRole(ADMINISTRATOR)

						// All other requests must be authenticated
						.anyRequest().authenticated())
				// Adding our custom JWT filter before the standard
				// UsernamePasswordAuthenticationFilter
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