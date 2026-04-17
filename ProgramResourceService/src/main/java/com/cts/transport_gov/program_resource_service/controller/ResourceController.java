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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.program_resource_service.dto.ProgramUtilization;
import com.cts.transport_gov.program_resource_service.dto.ResourceCreateRequest;
import com.cts.transport_gov.program_resource_service.dto.ResourceResponse;
import com.cts.transport_gov.program_resource_service.enums.ResourceStatus;
import com.cts.transport_gov.program_resource_service.service.IResourceService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ResourceController {
	private final IResourceService resourceService;

	/*
	 * Method: GET Description: It fetch all resources return:
	 * ResponseEntity<ApiResponse> type
	 */
	@GetMapping("/")
	public ResponseEntity<List<ResourceResponse>> getAllResources() {
		log.info("All resources feteched!");
		return ResponseEntity.ok(resourceService.getAllResouces());
	}

	/*
	 * Method: GET Argument: resourceId - Type- Long Description: It fetch all
	 * resource by provided id return: ResponseEntity<ApiResponse> type
	 */
	@GetMapping("/{resourceId}")
	public ResponseEntity<ResourceResponse> getResource(@PathVariable Long resourceId) {
		log.info("Resource with id {} feteched", resourceId);
		return ResponseEntity.ok(resourceService.getResource(resourceId));
	}

	/*
	 * Method: GET Argument: resourceId - Type- Long Description: It fetch all
	 * resource by provided program id return: ResponseEntity<ApiResponse> type
	 */
	@GetMapping("")
	public ResponseEntity<List<ResourceResponse>> getAllResourcesByProgram(
			@RequestParam @NotNull(message = "Program id is required") Long programId) {
		log.info("Resource with program id {} feteched", programId);
		return ResponseEntity.ok(resourceService.getAllResoucesByProgramId(programId));
	}

	/*
	 * Method: POST Argument: Resource Request Dto Description: Accepts Resource
	 * Request Dto and call add Resource method return: ResponseEntity<ApiResponse>
	 * type
	 */
	@PostMapping("/")
	public ResponseEntity<String> addResource(
			@RequestBody @NotNull(message = "Resource data required") ResourceCreateRequest createRequest) {
		log.info("new Resource added");
		return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.addResouce(createRequest));
	}

	/*
	 * Method: PATCH Argument: Resource id and Status Description: Accepts Resource
	 * id and status and call service method return: ResponseEntity<ApiResponse>
	 * type
	 */
	@PatchMapping("/{resourceId}")
	public ResponseEntity<String> changeResourceStatus(
			@PathVariable @NotNull(message = "Resource Id required") Long resourceId,
			@NotNull(message = "Resource Status required") @RequestParam ResourceStatus status) {
		log.info("Resource status changed id:{} status:{}", resourceId, status);
		return ResponseEntity.ok(resourceService.changeResourcStatus(resourceId, status));

	}

	/*
	 * Method: PATCH Argument: Resource id Description: Accepts Resource id and call
	 * service method return: ResponseEntity<ApiResponse> type
	 */
	@PatchMapping("/{resourceId}/allocate")
	public ResponseEntity<String> allocateResource(
			@PathVariable @NotNull(message = "Resource Id required") Long resourceId) {
		log.info("Resource allocated id:{} ", resourceId);
		return ResponseEntity.ok(resourceService.allocateResouce(resourceId));

	}

	/*
	 * Method: DELETE Argument: Resource id Description: Accepts Resource id and
	 * call deleteResource() service method return: ResponseEntity<ApiResponse> type
	 */
	@DeleteMapping("/{resourceId}")
	public ResponseEntity<String> deleteResource(@PathVariable Long resourceId) {
		log.info("Resource deleted with id:{} ", resourceId);
		return ResponseEntity.ok(resourceService.deleteResouce(resourceId));

	}

	// 29. GET /resources/{programId}/utilizations → Log resource usage
	@GetMapping("/{programId}/utilizations")
	public ResponseEntity<ProgramUtilization> getUtilizedResourcesForProgram(@PathVariable Long programId) {
		log.info("All resources feteched!");
		return ResponseEntity.ok(resourceService.getResourceUtilization(programId));
	}

}
