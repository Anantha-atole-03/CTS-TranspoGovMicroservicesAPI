
package com.cts.transport_gov.citizen_service.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String token;
	private String email;
	private String role;
	private String status; // This will receive "ACTIVE"
	private String message; // This will receive "Token is valid"
}
