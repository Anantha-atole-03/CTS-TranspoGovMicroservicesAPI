package com.cts.transport_gov.compliance_audit_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.compliance_audit_service.enums.ComplianceResultStatus;
import com.cts.transport_gov.compliance_audit_service.model.ComplianceRecord;

@Repository
public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Long> {

	List<ComplianceRecord> findByEntityId(Long entityId);

	int countByResult(ComplianceResultStatus result);

//	List<ComplianceRecord> findByType(ComplianceType Type);

}