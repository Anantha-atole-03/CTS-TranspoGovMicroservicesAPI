package com.cts.transport_gov.compliance_audit_service.exceptions;

import java.time.LocalDateTime;

import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.transport_gov.compliance_audit_service.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//	/* ================== ROUTE ================== */

	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ErrorResponse> handleRouteService(ServiceUnavailableException ex) {

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(new ErrorResponse(ex.getMessage(), 503, LocalDateTime.now()));
	}
//
//	/* ================== PROGRAM ================== */
//
//	@ExceptionHandler(ProgramServiceUnavailableException.class)
//	public ResponseEntity<ErrorResponse> handleProgramService(ProgramServiceUnavailableException ex) {
//
//		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//				.body(new ErrorResponse(ex.getMessage(), 503, LocalDateTime.now()));
//	}
//
//	/* ================== TICKET ================== */
//
//	@ExceptionHandler(TicketServiceUnavailableException.class)
//	public ResponseEntity<ErrorResponse> handleTicketUnavailable(TicketServiceUnavailableException ex) {
//
//		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//				.body(new ErrorResponse(ex.getMessage(), 503, LocalDateTime.now()));
//	}
//
	/* ================== CIRCUIT BREAKER SAFETY ================== */

	@ExceptionHandler(NoFallbackAvailableException.class)
	public ResponseEntity<ErrorResponse> handleNoFallback(NoFallbackAvailableException ex) {

		log.error("Circuit breaker fallback missing", ex);

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(
				"Dependent service is unavailable. Please try again later", 503, LocalDateTime.now()));
	}

	/* ================== GENERIC (ALWAYS LAST) ================== */

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e) {

		log.error(e.getClass() + " : " + e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}
}
