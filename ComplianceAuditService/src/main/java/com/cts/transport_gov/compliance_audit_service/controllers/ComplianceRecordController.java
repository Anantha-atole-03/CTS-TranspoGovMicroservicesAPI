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

import com.cts.transport_gov.compliance_audit_service.dto.ComplianceCreateRequest;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceResponse;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceUpdate;
import com.cts.transport_gov.compliance_audit_service.service.ComplianceRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/compliance")
@RequiredArgsConstructor

public class ComplianceRecordController {

	private final ComplianceRecordService service;

	@GetMapping("/")
	public ResponseEntity<List<ComplianceResponse>> getAll() {

		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ComplianceResponse> getById(@PathVariable Long id) {

		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping("/save")
	public ResponseEntity<String> create(@RequestBody ComplianceCreateRequest body) {

		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(body));
	}

	@PatchMapping("/update/{id}")
	public ResponseEntity<ComplianceResponse> update(@PathVariable Long id, @RequestBody ComplianceUpdate body) {

		return ResponseEntity.ok(service.update(id, body));
	}

	/*
	 * DELETE /compliances/delete/id
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		return ResponseEntity.ok(service.delete(id));
	}

	/*
	 * GET /compliances/getByEntity/id
	 * 
	 */
	@GetMapping("/getByEntity/{entityId}")
	public ResponseEntity<List<ComplianceResponse>> findByEntityId(@PathVariable("entityId") Long entityId) {

		return ResponseEntity.ok(service.findByEntityId(entityId));
	}
//
//	@GetMapping("/summary")
//	public ResponseEntity<ApiResponse<?>> getCount() {
//		return ResponseEntity
//				.ok(new ApiResponse<>("Count fetched!", HttpStatus.OK.value(), service.getStatusWiseCount()));
//	}

}