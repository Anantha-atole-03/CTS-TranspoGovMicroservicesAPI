package com.cts.transport_gov.api_gateway.security;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtUtil {
	@Value("${jwt.secret}")

	private String secret;

	public void validateToken(final String token) {

		Jwts.parserBuilder()

				.setSigningKey(getSignKey())

				.build()

				.parseClaimsJws(token);

	}

	private Key getSignKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secret);

		return Keys.hmacShaKeyFor(keyBytes);

	}

}