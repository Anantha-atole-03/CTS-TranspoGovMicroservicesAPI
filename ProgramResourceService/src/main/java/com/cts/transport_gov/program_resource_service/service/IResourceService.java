package com.cts.transport_gov.program_resource_service.service;

import java.util.List;

import com.cts.transport_gov.program_resource_service.dto.ProgramUtilization;
import com.cts.transport_gov.program_resource_service.dto.ResourceCreateRequest;
import com.cts.transport_gov.program_resource_service.dto.ResourceResponse;
import com.cts.transport_gov.program_resource_service.enums.ResourceStatus;

public interface IResourceService {
	ResourceResponse getResource(Long resourceId);

	List<ResourceResponse> getAllResouces();

	List<ResourceResponse> getAllResoucesByProgramId(Long programId);

	String addResouce(ResourceCreateRequest createRequest);

	String allocateResouce(Long resourceId);

	String changeResourcStatus(Long resourceId, ResourceStatus resourceStatus);

	String deleteResouce(Long resourceId);

	ProgramUtilization getResourceUtilization(Long progrmaId);

}
