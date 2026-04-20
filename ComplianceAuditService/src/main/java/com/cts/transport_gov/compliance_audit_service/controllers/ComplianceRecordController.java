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
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceCreateRequest;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceResponse;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceUpdate;
import com.cts.transport_gov.compliance_audit_service.service.ComplianceRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/compliances")
@RequiredArgsConstructor
public class ComplianceRecordController {

	private final ComplianceRecordService service;

	@GetMapping("/")
	public ResponseEntity<ApiResponse<List<ComplianceResponse>>> getAll() {
		List<ComplianceResponse> data = service.findAll();
		return ResponseEntity
				.ok(new ApiResponse<List<ComplianceResponse>>("Records fetched!", HttpStatus.OK.value(), data));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ComplianceResponse>> getById(@PathVariable Long id) {
		ComplianceResponse data = service.findById(id);
		return ResponseEntity.ok(new ApiResponse<>("Record fetched!", HttpStatus.OK.value(), data));
	}

	@PostMapping("/save")
	public ResponseEntity<ApiResponse<String>> create(@RequestBody ComplianceCreateRequest body) {
		String message = service.create(body);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(message, HttpStatus.CREATED.value(), null));
	}

	@PatchMapping("/update/{id}")
	public ResponseEntity<ApiResponse<ComplianceResponse>> update(@PathVariable Long id,
			@RequestBody ComplianceUpdate body) {
		ComplianceResponse updated = service.update(id, body);
		return ResponseEntity.ok(new ApiResponse<>("Record updated successfully", HttpStatus.OK.value(), updated));
	}

	/*
	 * DELETE /compliances/delete/id
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
		String message = service.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(message, HttpStatus.OK.value(), null));
	}

	/*
	 * GET /compliances/getByEntity/id
	 * 
	 */
	@GetMapping("/getByEntity/{entityId}")
	public ResponseEntity<ApiResponse<List<ComplianceResponse>>> findByEntityId(
			@PathVariable("entityId") Long entityId) {
		List<ComplianceResponse> list = service.findByEntityId(entityId);
		return ResponseEntity.ok(new ApiResponse<>("Records fetched by entityId!", HttpStatus.OK.value(), list));
	}
//
//	@GetMapping("/summary")
//	public ResponseEntity<ApiResponse<?>> getCount() {
//		return ResponseEntity
//				.ok(new ApiResponse<>("Count fetched!", HttpStatus.OK.value(), service.getStatusWiseCount()));
//	}

}