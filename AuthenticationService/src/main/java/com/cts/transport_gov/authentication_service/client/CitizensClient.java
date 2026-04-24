package com.cts.transport_gov.authentication_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign Client for communicating with the Citizen Microservice.
 * This client allows the Authentication Service to verify if a citizen 
 * already exists in the primary citizen registry during the signup process.
 */
@FeignClient(name = "citizen-service") // Name must match the entry in Eureka/Service Registry
public interface CitizensClient {

    /**
     * Inter-service call to verify a citizen's existence via phone number.
     * * @param phone The mobile number to check in the citizen database.
     * @return Boolean true if the citizen exists, false otherwise.
     */
    @GetMapping("/api/citizens/exists-by-phone")
    Boolean getCitizenByPhone(@RequestParam("phone") String phone); 
}