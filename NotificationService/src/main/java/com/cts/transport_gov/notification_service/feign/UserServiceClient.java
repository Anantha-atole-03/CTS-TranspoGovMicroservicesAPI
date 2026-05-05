package com.cts.transport_gov.notification_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.transport_gov.notification_service.dto.UserDTO;

@FeignClient(name = "AUTHENTICATIONSERVICE")
public interface UserServiceClient {

	@GetMapping("/users/{id}")
	UserDTO getUserById(@PathVariable Long id);

	@GetMapping("/users/email/{email}")
	UserDTO getUserByEmail(@PathVariable String email);

}
