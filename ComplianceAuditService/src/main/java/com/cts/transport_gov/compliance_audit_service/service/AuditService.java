package com.cts.transport_gov.compliance_audit_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.compliance_audit_service.client.EntityFeignClient;
import com.cts.transport_gov.compliance_audit_service.dto.AuditResponse;
import com.cts.transport_gov.compliance_audit_service.dto.CreateAuditRequest;
import com.cts.transport_gov.compliance_audit_service.dto.GenerateReportResponse;
import com.cts.transport_gov.compliance_audit_service.dto.UpdateAuditRequest;
import com.cts.transport_gov.compliance_audit_service.dto.UserResponse;
import com.cts.transport_gov.compliance_audit_service.enums.AuditStatus;
import com.cts.transport_gov.compliance_audit_service.exceptions.AuditNotFoundException;
import com.cts.transport_gov.compliance_audit_service.model.Audit;
import com.cts.transport_gov.compliance_audit_service.repositories.AuditRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService implements IAuditService {

	private final AuditRepository auditRepository;
	private final EntityFeignClient entityFeignClient;
	private final ModelMapper modelMapper;

	/* ---------------- FIND ALL ---------------- */

	@Override
	public List<AuditResponse> findAll() {
		log.info("Fetching all audit records");
		List<AuditResponse> result = auditRepository.findAll().stream().map(this::toAuditResponse)
				.collect(Collectors.toList());
		log.info("Fetched {} audit records", result.size());
		return result;
	}

	/* ---------------- FIND BY ID ---------------- */

	@Override
	public AuditResponse findById(Long id) {
		log.info("Fetching audit by id: {}", id);
		Audit audit = auditRepository.findById(id).orElseThrow(() -> {
			log.warn("Audit not found for id: {}", id);
			return new AuditNotFoundException("Audit Record not found");
		});
//		log.info("Fetched audit id: {}", audit.getId());
		return toAuditResponse(audit);
	}

	/* ---------------- CREATE ---------------- */

	@Override
	public AuditResponse create(CreateAuditRequest req) {

		log.info("Creating audit for officerId: {}", req.getOfficerId());

		// ✅ Fetch officer via Feign client
		ResponseEntity<UserResponse> officer = entityFeignClient.getuser(req.getOfficerId());

		if (officer == null) {
			throw new AuditNotFoundException("Aduit officer not found with id: " + req.getOfficerId());
		}

		// ✅ Map request → entity
		Audit audit = modelMapper.map(req, Audit.class);

		// ✅ Explicit business logic
		audit.setOfficer_id(officer.getBody().getUserId());// store ONLY the ID (best practice)
		audit.setStatus(AuditStatus.OPEN);
		audit.setStartedAt(LocalDateTime.now());

		Audit saved = auditRepository.save(audit);
		log.info("Audit created with id: {}", saved.getId());

		return toAuditResponse(saved);
	}

	/* ---------------- UPDATE ---------------- */

	@Override
	public AuditResponse update(Long id, UpdateAuditRequest req) {
		log.info("Updating audit id: {}", id);

		Audit existing = auditRepository.findById(id).orElseThrow(() -> {
			log.warn("Audit not found for update, id: {}", id);
			return new AuditNotFoundException("Audit Record not found");
		});

		existing.setScope(req.getScope());
		existing.setFindings(req.getFindings());

		if (req.getStatus() != null) {
			existing.setStatus(req.getStatus());
			if (req.getStatus() == AuditStatus.CLOSED) {
				existing.setClosedAt(LocalDateTime.now());
			}
		}

		Audit updatedAudit = auditRepository.save(existing);
		log.info("Audit updated successfully, id: {}", updatedAudit.getId());

		return toAuditResponse(updatedAudit);
	}

	/* ---------------- DELETE ---------------- */

	@Override
	public String delete(Long id) {
		log.info("Deleting audit record id: {}", id);

		Audit audit = auditRepository.findById(id).orElseThrow(() -> {
			log.warn("Audit not found for deletion, id: {}", id);
			return new AuditNotFoundException("Audit Record not found");
		});

		auditRepository.deleteById(audit.getId());
		log.info("Audit record deleted, id: {}", audit.getId());

		return "Record deleted Successfully";
	}

	/* ---------------- REPORT ---------------- */

	@Override
	public GenerateReportResponse generateReport(Long auditId) {
		log.info("Generating report for auditId: {}", auditId);

		Audit audit = auditRepository.findById(auditId)
				.orElseThrow(() -> new AuditNotFoundException("Audit not found"));

		String url = "https://reports.transpogov.local/audits/" + audit.getId() + "/report-"
				+ System.currentTimeMillis() + ".pdf";

		GenerateReportResponse resp = new GenerateReportResponse();
		resp.setAuditId(audit.getId());
		resp.setReportUrl(url);
		resp.setGeneratedAt(LocalDateTime.now());

		log.info("Report generated for auditId: {}", auditId);
		return resp;
	}

	/* ---------------- COUNT ---------------- */

	@Override
	public Long getCount() {
		Long count = auditRepository.count();
		log.info("Audit records count: {}", count);
		return count;
	}

	@Override
	public Map<AuditStatus, Long> getStatusWiseCount() {
		log.info("Fetching audit count grouped by status");
		return auditRepository.findStatusCount().stream()
				.collect(Collectors.toMap(obj -> (AuditStatus) obj[0], obj -> (Long) obj[1]));
	}

	/* ---------------- CLOSE AUDIT ---------------- */

	@Override
	public AuditResponse closeAudit(Long auditId) {
		log.info("Closing audit id: {}", auditId);

		Audit audit = auditRepository.findById(auditId).orElseThrow(() -> {
			log.warn("Audit not found for closeAudit, id: {}", auditId);
			return new AuditNotFoundException("Audit not found: " + auditId);
		});

		if (audit.getStatus() == AuditStatus.CLOSED) {
			throw new RuntimeException("Audit is already closed");
		}

		audit.setStatus(AuditStatus.CLOSED);
		audit.setClosedAt(LocalDateTime.now());

		Audit saved = auditRepository.save(audit);
		log.info("Audit closed id: {}, closedAt: {}", saved.getId(), saved.getClosedAt());

		return toAuditResponse(saved);
	}

	/* ---------------- HELPER ---------------- */

	private AuditResponse toAuditResponse(Audit audit) {

		// ✅ ModelMapper used (entity → response)
		AuditResponse dto = modelMapper.map(audit, AuditResponse.class);
		return dto;
	}
}
