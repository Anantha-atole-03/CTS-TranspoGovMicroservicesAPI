package com.cts.transport_gov.authentication_service.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom handler for Authorization failures. This class is triggered when a
 * user is successfully authenticated (logged in) but does not possess the
 * required UserRole/Authority to access a specific endpoint.
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * Handles the forbidden access scenario (HTTP 403). Responds with a structured
	 * JSON error message when permissions are insufficient.
	 * 
	 * @param request               The incoming HttpServletRequest.
	 * @param response              The HttpServletResponse to modify.
	 * @param accessDeniedException The exception indicating the authorization
	 *                              failure.
	 * @throws IOException If an input or output exception occurs while writing to
	 *                     the response.
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {

		// Set the status to 403 Forbidden (User is logged in, but not allowed here)
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		// Ensure the client knows the body contains JSON data
		response.setContentType("application/json");

		// Write the custom error response body
		response.getWriter().write("""
				    {
				      "error": "You do not have permission to access this resource"
				    }
				""");
	}
}