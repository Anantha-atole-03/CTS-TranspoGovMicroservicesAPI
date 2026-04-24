package com.cts.transport_gov.program_resource_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.program_resource_service.dto.ProgramCreateRequest;
import com.cts.transport_gov.program_resource_service.dto.ProgramResponse;
import com.cts.transport_gov.program_resource_service.dto.ProgramUpdateRequest;
import com.cts.transport_gov.program_resource_service.enums.ProgramStatus;
import com.cts.transport_gov.program_resource_service.service.ITransportProgramService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/programs")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TransportProgramController {

	private final ITransportProgramService programService;

	@GetMapping("/count")
	public ResponseEntity<Integer> getProgramCount() {
		return ResponseEntity.status(HttpStatus.OK).body(programService.programCount());
	}

	/*
	 * Method: GET Argument: N/A Description: It fetch all Programs by return:
	 * ResponseEntity<ApiResponse> type
	 */
	@GetMapping("/")
	public ResponseEntity<List<ProgramResponse>> getAllPrograms() {
		return ResponseEntity.ok(programService.getAllPrograms());
	}

	/*
	 * Method: GET Argument: program Id - Type- Long Description: It fetch Programs
	 * by Id return: ResponseEntity<ApiResponse> type
	 */
	@GetMapping("/{programId}")
	public ResponseEntity<ProgramResponse> getProgram(
			@NotNull(message = "Program id should be provided") @PathVariable Long programId) {
		log.info("Program With Id {} fetching", programId);
		return ResponseEntity.ok(programService.getProgram(programId));

	}

	/*
	 * Method: POST Argument:ProgramCreateRequest Description:Add New Program
	 * return: ResponseEntity<ApiResponse> type
	 */

	@PostMapping("/")
	@Validated
	public ResponseEntity<String> addProgram(
			@NotNull(message = "Program details should be provided") @RequestBody ProgramCreateRequest program) {
		log.info("Program With Id {} creating");
		return ResponseEntity.status(HttpStatus.CREATED).body(programService.addProgram(program));
	}

	/*
	 * Method: PATCH Argument:ProgramId Description:Program submitted for approval
	 * return: ResponseEntity<ApiResponse> type
	 */
	@PatchMapping("/{programId}/submit")
	public ResponseEntity<String> submitForApproval(
			@NotNull(message = "Program id should be provided") @PathVariable Long programId) {
		log.info("Program With Id {} submitted to approval", programId);
		return ResponseEntity.status(HttpStatus.OK).body(programService.submitForApproval(programId));
	}

	/*
	 * Method: PATCH Argument:ProgramId Description:Program approve with given id
	 * return: ResponseEntity<ApiResponse> type
	 */
	@PatchMapping("/{programId}/approve")
	public ResponseEntity<String> approveProgram(
			@NotNull(message = "Program id should be provided") @PathVariable Long programId) {
		log.info("Program With Id {} approving", programId);
		return ResponseEntity.status(HttpStatus.OK).body(programService.approveProgram(programId));
	}

	/*
	 * Method: PATCH Argument:Program Id, Status Description:Program status changes
	 * to give status return: ResponseEntity<ApiResponse> type
	 */
	@PatchMapping("/{programId}/status/{status}")
	public ResponseEntity<String> changeProgramStatus(
			@NotNull(message = "Program id should be provided") @PathVariable Long programId,
			@NotNull(message = "Program status should be provided") @PathVariable ProgramStatus status) {
		log.info("Program With Id {} Status {} changed", programId, status);
		return ResponseEntity.status(HttpStatus.OK).body(programService.changeProgramStatus(programId, status));

	}

	/*
	 * Method: PUT Argument:ProgramId Description:Update Program with id return:
	 * ResponseEntity<ApiResponse> type
	 */
	@PutMapping("/{programId}")
	public ResponseEntity<ProgramResponse> updateProgram(
			@NotNull(message = "Program id should be provided") @PathVariable Long programId,
			@RequestBody ProgramUpdateRequest updateRequest) {
		log.info("Program With Id {} updating", programId);
		return ResponseEntity.status(HttpStatus.OK).body(programService.updateProgram(programId, updateRequest));
	}
	/*
	 * Method: DELETE Argument:ProgramId Description:DELETE Program with id return:
	 * ResponseEntity<ApiResponse> type
	 */

	@DeleteMapping("/{programId}")
	public ResponseEntity<ProgramResponse> deleteProgram(
			@NotNull(message = "Program id should be provided") @PathVariable Long programId) {
		log.info("Program With Id {} deleting", programId);
		return ResponseEntity.status(HttpStatus.OK).body(programService.deleteProgram(programId));
	}

}
