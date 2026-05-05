package com.cts.transport_gov.compliance_audit_service.service;

import java.util.List;

import com.cts.transport_gov.compliance_audit_service.dto.ComplianceCreateRequest;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceResponse;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceUpdate;
import com.cts.transport_gov.compliance_audit_service.enums.ComplianceType;

public interface IComplianceRecordService {
	List<ComplianceResponse> findAll();

	List<ComplianceResponse> findByEntityId(Long entityId);

	Long getCount();

	ComplianceResponse update(Long id, ComplianceUpdate record);

	String delete(Long id);

	ComplianceResponse findById(Long id);

	int getComplianceAlerts();

//	void sendComplianceNotification(ComplianceRecord record);

	String create(ComplianceCreateRequest record);

	List<ComplianceResponse> findByType(ComplianceType type);

//	Map<ComplianceResultStatus, Long> getStatusWiseCount();

}
