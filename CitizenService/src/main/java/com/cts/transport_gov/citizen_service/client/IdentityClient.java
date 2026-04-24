package com.cts.transport_gov.citizen_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.transport_gov.citizen_service.dtos.AuthResponse;

@FeignClient(name = "AuthenticationService")
public interface IdentityClient {
 
    @GetMapping("/auth/validate")
    AuthResponse validateToken(@RequestHeader("Authorization") String token);
}