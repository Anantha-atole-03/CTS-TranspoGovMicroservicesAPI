package com.cts.transport_gov.program_resource_service.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.program_resource_service.dto.ProgramUtilization;
import com.cts.transport_gov.program_resource_service.dto.ResourceCreateRequest;
import com.cts.transport_gov.program_resource_service.dto.ResourceResponse;
import com.cts.transport_gov.program_resource_service.enums.ProgramStatus;
import com.cts.transport_gov.program_resource_service.enums.ResourceStatus;
import com.cts.transport_gov.program_resource_service.exception.ProgramNotFoundException;
import com.cts.transport_gov.program_resource_service.exception.ResourceAllocationException;
import com.cts.transport_gov.program_resource_service.exception.ResourceNotFoundException;
import com.cts.transport_gov.program_resource_service.model.Resource;
import com.cts.transport_gov.program_resource_service.model.TransportProgram;
import com.cts.transport_gov.program_resource_service.respository.IResourceRepository;
import com.cts.transport_gov.program_resource_service.respository.ITransportProgramRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer implementation for Transport Program operations.
 * 
 * @Service → Marks this class as a Spring service component. @Transactional→
 *          Ensures all DB operations are executed within a transaction.
 * @RequiredArgsConstructor → Generates constructor for final fields (Dependency
 *                          Injection).
 * @Slf4j → Enables logging using SLF4J.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements IResourceService {

	private final IResourceRepository resourceRepository;
	private final ITransportProgramRepository programRepository;
	// ModelMapper for converting Entity ↔ DTO
	private final ModelMapper modelMapper;

	/**
	 * Fetch a single resource by its ID. Throws ResourceNotFoundException if
	 * resource does not exist.
	 */
	@Override
	public ResourceResponse getResource(Long resourceId) {
		Resource resource = resourceRepository.findById(resourceId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
		ResourceResponse response = modelMapper.map(resource, ResourceResponse.class);
		// Explicitly setting programId (nested object mapping)
		response.setProgramId(resource.getProgram().getProgramId());
		return response;
	}

	/**
	 * Fetch all resources. Converts entity list into response DTO list.
	 */
	@Override
	public List<ResourceResponse> getAllResouces() {
		log.info("All Resource Fetched!");
		return resourceRepository.findAll().stream().map(resource -> {
			ResourceResponse response = modelMapper.map(resource, ResourceResponse.class);
			response.setProgramId(resource.getProgram().getProgramId());
			return response;
		}).toList();

	}

	/**
	 * Fetch all resources associated with a specific program ID.
	 */
	@Override
	public List<ResourceResponse> getAllResoucesByProgramId(Long programId) {
		log.info("Id: {} Resource Fetched!", programId);
		return resourceRepository.findByProgramProgramId(programId).stream().map(resource -> {
			ResourceResponse response = modelMapper.map(resource, ResourceResponse.class);
			response.setProgramId(resource.getProgram().getProgramId());
			return response;
		}).toList();
	}

	/**
	 * Allocates a resource. Allowed only if resource status is IN_PROCUREMENT.
	 */
	@Override
	public String allocateResouce(Long resourceId) {
		Resource resource = resourceRepository.findById(resourceId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found respective id"));
		// Business rule: allocation allowed only in IN_PROCUREMENT state
		if (resource.getStatus().equals(ResourceStatus.IN_PROCUREMENT)) {
			resource.setStatus(ResourceStatus.ASSIGNED);
			resourceRepository.save(resource);
			log.info("Id: {} Resource allocated!", resource.getResourceId());
			return "Resource Allocated successfully";
		} else {
			throw new ResourceAllocationException("Resource already allocated or in use");
		}

	}

	/**
	 * Adds a new resource. Default status is set to IN_PROCUREMENT.
	 */
	@Override
	public String addResouce(ResourceCreateRequest createRequest) {
		Resource resource = modelMapper.map(createRequest, Resource.class);
		TransportProgram program = programRepository.findById(createRequest.getProgramId())
				.orElseThrow(() -> new ProgramNotFoundException("Program Not found"));
		if (program.getStatus().equals(ProgramStatus.APPROVED)
				|| program.getStatus().equals(ProgramStatus.IN_PROGRESS)) {
			double expensedBugdet = resourceRepository.getTotalBudgetByProgramId(program.getProgramId())
					+ createRequest.getBudget();

			if (expensedBugdet <= program.getBudget()) {

				// Set default status on creation
				resource.setStatus(ResourceStatus.IN_PROCUREMENT);
				resource.setProgram(program);

				resourceRepository.save(resource);
				log.info("Id: {} Resource saved!", resource.getResourceId());
				return "Resource added succesfully!";
			} else {
				throw new ResourceAllocationException("You can assign Resource, resource is exceeding budget");

			}
		} else {
			throw new ResourceAllocationException(
					"You can assign Resource, Program is in " + program.getStatus().toString() + " phase");
		}

	}

	/**
	 * Change resource status. No transition validation is applied here.
	 */
	@Override
	public String changeResourcStatus(Long resourceId, ResourceStatus resourceStatus) {
		Resource resource = resourceRepository.findById(resourceId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
		resource.setStatus(resourceStatus);
		resourceRepository.save(resource);
		log.info("Id: {} Resource status changed to {}!", resource.getResourceId(), resource);
		return "Resource status updated successfully";
	}

	/**
	 * Deletes a resource by ID.
	 */
	@Override
	public String deleteResouce(Long resourceId) {
		Resource resource = resourceRepository.findById(resourceId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
		log.info("Id: {} Resource deleted!", resource.getResourceId());
		resourceRepository.delete(resource);
		return "Resource deleted successfullty";
	}

	@Override
	public ProgramUtilization getResourceUtilization(Long programId) {

		TransportProgram program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Transport Program not found with id: " + programId));

		List<Resource> resources = resourceRepository.findByProgramProgramId(programId);

		// Budget Calculations
		double allocatedBudget = program.getBudget();

		double utilizedBudget = resources.stream().mapToDouble(Resource::getBudget).sum();

		double remainingBudget = allocatedBudget - utilizedBudget;

		double budgetUtilizationPercentage = allocatedBudget > 0 ? (utilizedBudget / allocatedBudget) * 100 : 0.0;

		// Resource Calculations
		int totalResourcesAllocated = resources.stream().mapToInt(Resource::getQuantity).sum();

		int totalResourcesUsed = resources.stream().filter(r -> r.getStatus() == ResourceStatus.IN_USE)
				.mapToInt(Resource::getQuantity).sum();

		return ProgramUtilization.builder().programId(program.getProgramId()).title(program.getTitle())
				.description(program.getDescription()).startDate(program.getStartDate()).endDate(program.getEndDate())
				.status(program.getStatus()).allocatedBudget(allocatedBudget).utilizedBudget(utilizedBudget)
				.remainingBudget(remainingBudget).budgetUtilizationPercentage(round(budgetUtilizationPercentage))
				.totalResourcesAllocated(totalResourcesAllocated).totalResourcesUsed(totalResourcesUsed).build();
	}

	private double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

}
