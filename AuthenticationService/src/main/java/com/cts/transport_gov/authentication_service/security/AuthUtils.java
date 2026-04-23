package com.cts.transport_gov.authentication_service.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.model.Citizen;
import com.cts.transport_gov.authentication_service.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Utility component for JSON Web Token (JWT) management. Handles token
 * generation, validation, and claim extraction for the security filter and
 * services.
 */
@Component

public class AuthUtils {

	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * Converts the configured secret string into a cryptographic HMAC key.
	 * 
	 * @return A SecretKey for signing and parsing JWTs.
	 */
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Generates a signed JWT access token for an authenticated user or citizen.
	 * 
	 * @param userDetails The authenticated principal object.
	 * @return A compact, URL-safe JWT string.
	 * @throws IllegalArgumentException If the userDetails type is not User or
	 *                                  Citizen.
	 */

	public String generateAccessToken(UserDetails userDetails) {

		String phone;
		UserRole role;
		String id;

		// Identify the entity type to extract specific identification details
		if (userDetails instanceof User user) {
			phone = user.getPhone();
			role = user.getRole();
			id = user.getUserId().toString();

		} else if (userDetails instanceof Citizen citizen) {
			phone = citizen.getPhone();
			role = citizen.getRole();
			id = citizen.getCitizenId().toString();

		} else {
			// Fail-safe for unsupported security principal types
			throw new IllegalArgumentException("Unsupported UserDetails type");
		}

		/*
		 * Build the JWT: - setSubject: Stores the phone number (primary identifier). -
		 * claim("id"): Stores the database primary key. - claim("role"): Stores the
		 * user's authorization level. - setExpiration: Valid for 5 hours.
		 */

		return Jwts.builder().subject(phone).claim("id", id).claim("role", role).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + (5 * 60 * 60 * 1000))).signWith(getSecretKey())
				.compact();
	}

	/**
	 * Parses the provided JWT to extract the payload (Claims). This method
	 * implicitly validates the token's signature and expiration.
	 * 
	 * @param token The JWT string.
	 * @return The Claims object containing the token payload.
	 */

	private Claims getClaims(String token) {
		return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
	}

	/**
	 * Extracts the 'subject' (phone number) from the token.
	 * 
	 * @param token The JWT string.
	 * @return The phone number stored in the token.
	 */
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	/**
	 * Extracts the 'role' claim from the token.
	 * 
	 * @param token The JWT string.
	 * @return The UserRole as a string.
	 */
	public String getRole(String token) {
		return getClaims(token).get("role", String.class);
	}

}