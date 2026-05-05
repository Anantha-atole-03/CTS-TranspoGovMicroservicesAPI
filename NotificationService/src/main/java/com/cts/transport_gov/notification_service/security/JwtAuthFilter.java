package com.cts.transport_gov.notification_service.security;

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

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String uri = request.getRequestURI();
            log.info("Incoming request: {}", uri);

            // ✅ ✅ FIX: Allow OTP without gateway / JWT
            if ("/notification/otp".equals(uri)) {
                filterChain.doFilter(request, response);
                return;
            }

            // ✅ Allow swagger
            if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs")) {
                filterChain.doFilter(request, response);
                return;
            }

            // ✅ Existing gateway protection (UNCHANGED)
            if (!"gateway".equals(request.getHeader("X-Internal-Secret"))) {
                log.warn("Forbidden request: not from gateway");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // ✅ Extract headers added by API Gateway
            String role = request.getHeader("X-Role");
            String username = request.getHeader("X-User-Phone");

            log.info("User role from gateway: {}", role);

            if (role != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username, null, authorities);

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("JwtAuthFilter exception", ex);
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
