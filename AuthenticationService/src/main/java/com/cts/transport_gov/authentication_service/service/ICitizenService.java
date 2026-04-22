package com.cts.transport_gov.authentication_service.service;

import com.cts.transport_gov.authentication_service.dto.CitizenCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenResponse;

public interface ICitizenService {
	CitizenResponse save(CitizenCreateRequest requestDto);
}
