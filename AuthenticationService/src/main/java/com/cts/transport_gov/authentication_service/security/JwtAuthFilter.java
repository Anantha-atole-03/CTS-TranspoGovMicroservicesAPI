package com.cts.transport_gov.authentication_service.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.cts.transport_gov.authentication_service.model.Citizen;
import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import com.cts.transport_gov.authentication_service.respository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Security filter that executes once per request to intercept and validate JWT
 * tokens. It extracts the identity from the token and populates the Spring
 * Security Context if the token is valid.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	// Services and repositories required to validate the token and load the
	// principal
	private final UserRepository userRepository;
	private final CitizenRepository citizenRepository;
	private final AuthUtils authUtils;
	private final HandlerExceptionResolver handlerExceptionResolver;

	/**
	 * Core filtering logic that runs before the request reaches the Controller.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			log.info("Incoming request: {}", request.getRequestURI());

			// 1. Extract the Authorization header from the request
			final String authHeader = request.getHeader("Authorization");

			// 2. Validate the header format (must start with "Bearer ")
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			// 3. Extract the actual token string (removing "Bearer " prefix)
			String token = authHeader.substring(7);

			/*
			 * 4. Parse the token to get the username (phone). If the token is tampered with
			 * or expired, Jwts.parserBuilder() inside authUtils will throw an exception
			 * caught by our catch block.
			 */
			String phone = authUtils.getUsername(token);

			// 5. If phone exists and user is not already authenticated in this session
			// context
			if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				// Check the Staff/Admin repository first
				Optional<User> userOpt = userRepository.findByPhone(phone);

				if (userOpt.isPresent()) {
					User user = userOpt.get();

					// Create authentication token for User
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
							user.getAuthorities());

					// Set the authentication in the Security Context
					SecurityContextHolder.getContext().setAuthentication(auth);

				} else {
					// If not a staff User, check the Citizen repository
					Citizen citizen = citizenRepository.findByPhone(phone)
							.orElseThrow(() -> new UsernameNotFoundException("No user found with phone: " + phone));

					// Create authentication token for Citizen
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(citizen, null,
							citizen.getAuthorities());

					// Set the authentication in the Security Context
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}

			// 6. Continue the filter chain to the next filter or controller
			filterChain.doFilter(request, response);

		} catch (Exception ex) {
			/*
			 * 7. If any error occurs (token expired, invalid signature, etc.), delegate the
			 * exception to the global handler to return a 401 response.
			 */
			handlerExceptionResolver.resolveException(request, response, null, ex);

		}
	}
}