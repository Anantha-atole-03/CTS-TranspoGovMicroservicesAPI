package com.cts.transport_gov.report_analytics_service.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

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

	private final HandlerExceptionResolver handlerExceptionResolver;

	/**
	 * Core filtering logic that runs before the request reaches the Controller.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			log.info("Incoming request: {}", request.getRequestURI());

			// check request comming through gateway only
			if (!"gateway".equals(request.getHeader("X-Internal-Secret"))) {
				log.info("forbidden request");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;

			}
//			requestBuilder.header("X-User-Id", claims.get("id").toString())
//			.header("X-User-Phone", claims.getSubject()).header("X-Role", claims.get("role").toString());

			String role = request.getHeader("X-Role");
			String username = request.getHeader("X-User-Phone");
			log.info(role);
			if (role != null) {
				List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
						authorities);
				SecurityContextHolder.getContext().setAuthentication(auth);

			}

			filterChain.doFilter(request, response);

		} catch (Exception ex) {
			/*
			 * If any error occurs (token expired, invalid signature, etc.), delegate the
			 * exception to the global handler to return a 401 response.
			 */
			log.debug("Ticket Auth filer exception:{}", ex.getMessage());
			handlerExceptionResolver.resolveException(request, response, null, ex);

		}
	}
}