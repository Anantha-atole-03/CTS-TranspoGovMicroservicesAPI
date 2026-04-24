package com.cts.transport_gov.authentication_service.config;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * COMMAND: Provide cryptographic services for generating, parsing, and validating JSON Web Tokens (JWT).
 * Logic: Uses HMAC SHA-256 signing to secure user identity and roles, enforcing time-based expiration 
 * and integrity checks across the distributed system.
 */
@Component
@Slf4j
public class JwtUtil {
    private static final String SEC = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SEC);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // --- NEW METHOD: Extract Role for your teammates ---
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.error("Failed to extract claims from token: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * COMMAND: Construct a signed JWT string for a validated user.
     */
    public String generateToken(String email, String role) {
        log.info("Generating JWT for user: {} with role: {}", email, role);
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- NEW OVERLOADED METHOD: Fixes the "not applicable" error ---
    // This version is used by the /auth/validate endpoint
    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            log.debug("Token signature and integrity validated successfully.");
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            throw e; 
        }
    }

    // This version is used during the Login process
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !extractExpiration(token).before(new Date()));
        if (!isValid) {
            log.warn("Security check failed: Token username mismatch or token expired for user {}", userDetails.getUsername());
        }
        return isValid;
    }
}