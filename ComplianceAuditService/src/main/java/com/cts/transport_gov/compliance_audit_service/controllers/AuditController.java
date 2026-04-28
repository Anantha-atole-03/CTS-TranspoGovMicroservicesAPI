package com.cts.transport_gov.compliance_audit_service.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.compliance_audit_service.dto.ApiResponse;
import com.cts.transport_gov.compliance_audit_service.dto.AuditResponse;
import com.cts.transport_gov.compliance_audit_service.dto.CreateAuditRequest;
import com.cts.transport_gov.compliance_audit_service.dto.UpdateAuditRequest;
import com.cts.transport_gov.compliance_audit_service.service.IAuditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

	private final IAuditService auditService;

	/* ========= POST /audits — Create audit ========= */
	@PostMapping("/create")
	public ResponseEntity create(@RequestBody CreateAuditRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(auditService.create(request));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		return ResponseEntity.ok(auditService.delete(id));
	}

	@PatchMapping("/update/{id}")
	public ResponseEntity<AuditResponse> update(@PathVariable long id, @RequestBody UpdateAuditRequest body) {
		return ResponseEntity.ok(auditService.update(id, body));
	}

	/* ========= GET /audits — List audits (filters, pagination) ========= */
	@GetMapping("/")
	public ResponseEntity<List> findAll() {
		return ResponseEntity.ok(auditService.findAll());
	}

	/* ========= GET /audits/{id} — Audit details + findings ========= */
	@GetMapping("/{id}")
	public ResponseEntity<AuditResponse> get(@PathVariable Long id) {

		return ResponseEntity.ok(auditService.findById(id));
	}

	/* ========= POST /audits/{id}/close — Close audit ========= */
	@GetMapping("/{id}/close")
	public ResponseEntity<ApiResponse<AuditResponse>> close(@PathVariable Long id) {
		AuditResponse dto = auditService.closeAudit(id);
		return ResponseEntity.ok(new ApiResponse<>("Audit closed!", HttpStatus.OK.value(), dto));
	}

	// GET /compliances/summary
	@GetMapping("/summary")
	public ResponseEntity<?> getCount() {
		return ResponseEntity.ok(auditService.getStatusWiseCount());
	}

}