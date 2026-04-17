package com.cts.transport_gov.program_resource_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<?> handleInvalidInputException(MethodArgumentNotValidException e) {
//
//		Map<String, String> errorMap = e.getFieldErrors().stream()
//				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//				.body(new ExceptionResponse(errorMap.get("name"), HttpStatus.NOT_FOUND.value()));
//	}

	@ExceptionHandler(ResourceAllocationException.class)
	public ResponseEntity<ExceptionResponse> handleResourceAllocationException(ResourceAllocationException e) {
		log.error(e.getClass() + " : " + e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(InvalidInputDataException.class)
	public ResponseEntity<ExceptionResponse> handleInvalidInputDataException(InvalidInputDataException e) {
		log.error(e.getClass() + " : " + e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
		log.error(e.getClass() + " : " + e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
	}

	@ExceptionHandler(ProgramNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleProgramNotFoundException(ProgramNotFoundException e) {
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

		System.out.println("" + e.getClass());
		log.error(e.getClass() + " : " + e.getMessage() + " ");

		log.error(e.getClass() + " : " + e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

}
