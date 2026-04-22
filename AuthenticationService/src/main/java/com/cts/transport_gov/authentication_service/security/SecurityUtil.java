package com.cts.transport_gov.authentication_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

	public static String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return (authentication != null) ? authentication.getName() : "SYSTEM";
	}
}