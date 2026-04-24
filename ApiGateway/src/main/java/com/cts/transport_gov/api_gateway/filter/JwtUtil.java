package com.cts.transport_gov.api_gateway.filter;
 
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component

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
 