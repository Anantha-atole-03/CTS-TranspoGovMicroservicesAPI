package com.cts.transport_gov.authentication_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * COMMAND: Intercept incoming HTTP requests to validate JWT credentials.
 * Logic: Extracts the Bearer token, verifies its integrity via JwtUtil, 
 * and populates the Spring SecurityContext if the token is valid.
 */
@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * COMMAND: Execute token validation logic for the current request execution.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String path = request.getServletPath();

        // Check for missing or malformed Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.trace("No JWT token found for request path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            String userEmail = jwtUtil.extractUsername(jwt);
            String role = jwtUtil.extractRole(jwt); 

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("JWT validated for user: {}. Setting security context with role: {}", userEmail, role);
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, List.of(new SimpleGrantedAuthority(role)));
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Replaced System.err with structured SLF4J logging
            log.error("JWT Authentication failed for path {}: {}", path, e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}