package com.cts.transport_gov.program_resource_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.program_resource_service.dto.ProgramCreateRequest;
import com.cts.transport_gov.program_resource_service.dto.ProgramResponse;
import com.cts.transport_gov.program_resource_service.dto.ProgramUpdateRequest;
import com.cts.transport_gov.program_resource_service.enums.ProgramStatus;
import com.cts.transport_gov.program_resource_service.exception.InvalidInputDataException;
import com.cts.transport_gov.program_resource_service.exception.ProgramNotFoundException;
import com.cts.transport_gov.program_resource_service.model.TransportProgram;
import com.cts.transport_gov.program_resource_service.respository.ITransportProgramRepository;

import jakarta.transaction.Transactional;
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
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransportProgramServiceImpl implements ITransportProgramService {

	private final ITransportProgramRepository programRepository;
	// ModelMapper for Entity ↔ DTO conversion
	private final ModelMapper modelMapper;

	/**
	 * Fetches all transport programs. Converts entity list to response DTO list.
	 */
	@Override
	public List<ProgramResponse> getAllPrograms() {

		return programRepository.findAll().stream()
				// Mapping each TransportProgram entity to ProgramResponse DTO
				.map(program -> modelMapper.map(program, ProgramResponse.class)).collect(Collectors.toList());

	}

	/**
	 * Fetch a single program by ID. Throws exception if program does not exist.
	 */
	@Override
	public ProgramResponse getProgram(Long programId) {
		TransportProgram program = programRepository.findById(programId)
				.orElseThrow(() -> new ProgramNotFoundException("Program not found!"));
		log.info("Id: {} Program Fetched!", programId);
		return modelMapper.map(program, ProgramResponse.class);
	}

	/**
	 * Creates a new program. Initial status is always set to DRAFT.
	 */
	@Override
	public String addProgram(ProgramCreateRequest program) {
		if (program.getEndDate().isBefore(program.getStartDate())) {
			throw new InvalidInputDataException("Start date should less than End date");
		}

		TransportProgram program2 = modelMapper.map(program, TransportProgram.class);
		program2.setStatus(ProgramStatus.DRAFT);
		programRepository.save(program2);
		log.info("Id: {} Program Saved!", program2.getProgramId());
		return "Program Successfuly Added!";
	}

	/**
	 * Submits a program for approval. Status changes from DRAFT → SUBMITTED.
	 */
	@Override
	public String submitForApproval(Long programId) {
		TransportProgram program = programRepository.findById(programId)
				.orElseThrow(() -> new ProgramNotFoundException("Program not found!"));
		program.setStatus(ProgramStatus.SUBMITTED);
		programRepository.save(program);
		log.info("Id: {} Program submitted for Approve!", program.getProgramId());
		return "Program successfully submitted for Approvel!";
	}

	/**
	 * Approves a submitted program.
	 */
	@Override
	public String approveProgram(Long programId) {
		TransportProgram program = programRepository.findById(programId)
				.orElseThrow(() -> new ProgramNotFoundException("Program not found!"));
		program.setStatus(ProgramStatus.APPROVED);
		programRepository.save(program);
		log.info("Id: {} Program Approved!", program.getProgramId());
		return "Program successfully Approved!";
	}

	/**
	 * Generic method to change program status. Prevents unnecessary updates if
	 * status is unchanged.
	 */
	@Override
	public String changeProgramStatus(Long programId, ProgramStatus newStatus) {

		TransportProgram program = programRepository.findById(programId)
				.orElseThrow(() -> new ProgramNotFoundException("Program not found: " + programId));

		ProgramStatus current = program.getStatus();
		// Avoid unnecessary DB update
		if (current == newStatus) {
			return "No change: program status is already " + current.name() + ".";
		}

//		validateTransition(current, newStatus, program);

		program.setStatus(newStatus);
		programRepository.save(program);

		return "Program status changed from " + current.name() + " to " + newStatus.name() + ".";
	}

	/**
	 * Deletes a program and returns deleted program details.
	 */
	@Override
	public ProgramResponse deleteProgram(Long programId) {
		TransportProgram program = programRepository.findById(programId)
				.orElseThrow(() -> new ProgramNotFoundException("Program not found!"));
		programRepository.delete(program);
		return modelMapper.map(program, ProgramResponse.class);
	}

	/**
	 * Updates program fields selectively (partial update). Only non-null /
	 * non-blank fields are updated.
	 */
	@Override
	public ProgramResponse updateProgram(Long programId, ProgramUpdateRequest updateRequest) {
		TransportProgram program = programRepository.findById(programId)
				.orElseThrow(() -> new ProgramNotFoundException("Program not found!"));
		if (!updateRequest.getTitle().isBlank()) {
			program.setTitle(updateRequest.getTitle());
		}
		if (!updateRequest.getDescription().isBlank()) {
			program.setDescription(updateRequest.getDescription());
		}
		if (updateRequest.getStartDate() != null) {
			program.setStartDate(updateRequest.getStartDate());
		}
		if (updateRequest.getEndDate() != null) {
			program.setEndDate(updateRequest.getEndDate());
		}
		programRepository.save(program);
		return modelMapper.map(program, ProgramResponse.class);
	}

	@Override
	public int programCount() {
		return (int) programRepository.count();
	}

}
