package com.cts.transport_gov.compliance_audit_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	

	@ExceptionHandler(ComplianceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleComplianceNotFoundException(ComplianceNotFoundException e) {
		log.error(e.getClass() + " : " + e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(AuditNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleAuditNotFoundException(AuditNotFoundException e) {
		log.error(e.getClass() + " : " + e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e) {
		log.error(e.getClass() + " : " + e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

}
