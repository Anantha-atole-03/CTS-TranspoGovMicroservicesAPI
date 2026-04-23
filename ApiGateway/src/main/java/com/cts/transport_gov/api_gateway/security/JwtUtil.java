package com.cts.transport_gov.api_gateway.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

	@Value("${jwt.secret}")
	private String jwtSecret;

	public Claims validateToken(String token) {
		try {
			log.info("Validating JWT token");

			return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();

		} catch (JwtException e) {
			log.warn("JWT validation failed: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * Converts the configured secret string into a cryptographic HMAC key.
	 * 
	 * @return A SecretKey for signing and parsing JWTs.
	 */
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
}