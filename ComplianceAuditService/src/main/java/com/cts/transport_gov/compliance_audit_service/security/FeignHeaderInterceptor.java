package com.cts.transport_gov.compliance_audit_service.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeignHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attributes == null) {
			return;
		}

		HttpServletRequest request = attributes.getRequest();

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			template.header("Authorization", authHeader);
		}

		// Forward required headers
		copyHeader(request, template, "X-Internal-Secret");
		copyHeader(request, template, "X-Role");
		copyHeader(request, template, "X-User-Phone");
	}

	private void copyHeader(HttpServletRequest request, RequestTemplate template, String headerName) {

		String headerValue = request.getHeader(headerName);
		if (headerValue != null) {
			template.header(headerName, headerValue);
		}
	}
}
