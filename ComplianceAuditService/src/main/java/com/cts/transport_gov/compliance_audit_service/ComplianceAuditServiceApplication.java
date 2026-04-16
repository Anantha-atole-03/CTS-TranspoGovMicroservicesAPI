package com.cts.transport_gov.compliance_audit_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ComplianceAuditServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplianceAuditServiceApplication.class, args);
	}

}
