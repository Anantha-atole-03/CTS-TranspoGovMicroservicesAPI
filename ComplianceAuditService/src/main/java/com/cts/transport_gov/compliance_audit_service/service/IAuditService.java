package com.cts.transport_gov.compliance_audit_service.service;

import java.util.List;
import java.util.Map;

import com.cts.transport_gov.compliance_audit_service.dto.AuditResponse;
import com.cts.transport_gov.compliance_audit_service.dto.CreateAuditRequest;
import com.cts.transport_gov.compliance_audit_service.dto.GenerateReportResponse;
import com.cts.transport_gov.compliance_audit_service.dto.UpdateAuditRequest;
import com.cts.transport_gov.compliance_audit_service.enums.AuditStatus;


public interface IAuditService {

	AuditResponse create(CreateAuditRequest req);

	String delete(Long id);

	AuditResponse findById(Long id);

	GenerateReportResponse generateReport(Long auditId);

	AuditResponse closeAudit(Long auditId);

	Map<AuditStatus, Long> getStatusWiseCount();

	AuditResponse update(Long id, UpdateAuditRequest req);

	Long getCount();

	List<AuditResponse> findAll();

}