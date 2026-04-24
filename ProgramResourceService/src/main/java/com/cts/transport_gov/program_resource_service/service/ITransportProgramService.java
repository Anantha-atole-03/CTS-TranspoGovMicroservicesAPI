package com.cts.transport_gov.program_resource_service.service;

import java.util.List;

import com.cts.transport_gov.program_resource_service.dto.ProgramCreateRequest;
import com.cts.transport_gov.program_resource_service.dto.ProgramResponse;
import com.cts.transport_gov.program_resource_service.dto.ProgramUpdateRequest;
import com.cts.transport_gov.program_resource_service.enums.ProgramStatus;

public interface ITransportProgramService {
	List<ProgramResponse> getAllPrograms();

	ProgramResponse getProgram(Long programId);

	String addProgram(ProgramCreateRequest program);

	String submitForApproval(Long programId);

	String approveProgram(Long programId);

	String changeProgramStatus(Long programId, ProgramStatus status);

	ProgramResponse deleteProgram(Long programId);

	ProgramResponse updateProgram(Long programId, ProgramUpdateRequest updateRequest);

	int programCount();
}
