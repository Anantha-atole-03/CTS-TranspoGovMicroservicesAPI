package com.cts.transport_gov.compliance_audit_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.compliance_audit_service.model.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

	@Query("""
			SELECT a.status, COUNT(a)
			FROM Audit a
			GROUP BY a.status
			""")
	List<Object[]> findStatusCount();

	long countByStatus(String status);

}
