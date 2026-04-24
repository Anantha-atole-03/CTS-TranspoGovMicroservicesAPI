package com.cts.transport_gov.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

		return http

				.csrf(ServerHttpSecurity.CsrfSpec::disable).formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

				.authorizeExchange(exchange -> exchange.pathMatchers("/**").permitAll().anyExchange().permitAll())

				.build();
	}
}