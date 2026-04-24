package com.cts.transport_gov.ticket_fare_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(TicketStatusException.class)
	public ResponseEntity<ExceptionResponse> handleTicketStatusException(TicketStatusException e) {
		log.error(e.getClass() + " : " + e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ExceptionResponse> handleTicketStatusException(ServiceUnavailableException e) {
		log.error(e.getClass() + " : " + e.getMessage());

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value()));
	}

	@ExceptionHandler(TicketNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleTicketNotFoundException(TicketNotFoundException e) {
		log.error(e.getClass() + " : " + e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
	}

	@ExceptionHandler(CitizenNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleCitizenNotFoundException(CitizenNotFoundException e) {
		log.error(e.getClass() + " : " + e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException e) {
		log.error(e.getClass() + " : " + e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
		StringBuilder errors = new StringBuilder();
		ex.getBindingResult().getFieldErrors().forEach(
				error -> errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
		log.error(ex.getClass() + " : " + errors);
		return new ResponseEntity<>("Validation error(s): " + errors.toString(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e) {

		log.error(e.getClass() + " : " + e.getMessage() + " ");

		log.error(e.getClass() + " : " + e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

}
