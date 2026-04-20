package com.cts.transport_gov.compliance_audit_service.exceptions;



public class AuditNotFoundException extends ComplianceNotFoundException {
	private static final long serialVersionUID = 1L;

    public AuditNotFoundException(String message) {
        super(message);
    }
}
