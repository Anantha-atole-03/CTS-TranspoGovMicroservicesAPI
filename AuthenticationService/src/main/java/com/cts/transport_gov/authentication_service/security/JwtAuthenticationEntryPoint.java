package com.cts.transport_gov.authentication_service.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom entry point for Authentication failures. This class is triggered when
 * an unauthenticated user attempts to access a protected resource without a
 * valid JWT, or if the provided token is expired or malformed.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/**
	 * Handles the commencement of an authentication failure (HTTP 401). This is the
	 * first point of failure for users who are not "logged in" correctly.
	 * 
	 * @param request       The incoming HttpServletRequest.
	 * @param response      The HttpServletResponse to modify.
	 * @param authException The exception indicating why authentication failed.
	 * @throws IOException If an input or output exception occurs while writing to
	 *                     the response.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		// Set the status to 401 Unauthorized (Identity could not be verified)
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		// Set content type to JSON so the client-side (React/Angular) can parse it
		// easily
		response.setContentType("application/json");

		// Write the JSON response body
		// This happens if the token is expired, missing, or invalid.
		response.getWriter().write("""
				    {
				      "error": "JWT token is missing or invalid"
				    }
				""");
	}
}