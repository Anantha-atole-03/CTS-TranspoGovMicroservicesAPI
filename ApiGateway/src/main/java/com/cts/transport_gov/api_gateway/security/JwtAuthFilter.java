package com.cts.transport_gov.api_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {
	private final JwtUtil jwtUtil;

	public JwtAuthFilter(JwtUtil jwtUtil) {
		super(Config.class);
		this.jwtUtil = jwtUtil;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {

			String path = exchange.getRequest().getURI().getPath();
			log.info("JwtAuthFilter applied for path: {}", path);

			ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate().header("X-Internal-Secret",
					"gateway");

			if (!path.startsWith("/auth")) {
				log.info("If JwtAuthFilter applied for path: {}", path);
				String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

				if (authHeader == null || !authHeader.startsWith("Bearer ")) {
					log.info("JwtAuthFilter not bearer");
					exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
					return exchange.getResponse().setComplete();
				}

				Claims claims;
				try {
					log.info("JwtAuthFilter Bearer");
					claims = jwtUtil.validateToken(authHeader.substring(7));
					log.info("Claims bearer: {}", path);
				} catch (Exception e) {
					log.info("Claims bearer exception: {}", e.getMessage());
					exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
					return exchange.getResponse().setComplete();
				}

				requestBuilder.header("X-User-Id", claims.get("id").toString())
						.header("X-User-Phone", claims.getSubject()).header("X-Role", claims.get("role").toString());
			}

			ServerHttpRequest modifiedRequest = requestBuilder.build();

//			log.warn("Final Gateway Headers: {}", modifiedRequest.getHeaders());

			return chain.filter(exchange.mutate().request(modifiedRequest).build());
		};
	}

	@Configuration
	public static class Config {
	}
}