package com.cts.transport_gov.authentication_service.service;

import java.util.List;

import com.cts.transport_gov.authentication_service.dto.CitizenCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenResponse;
import com.cts.transport_gov.authentication_service.dto.CitizenUpdateRequest;

public interface ICitizenService {
	CitizenResponse save(CitizenCreateRequest requestDto);

	CitizenResponse addCitizen(CitizenCreateRequest request);

	CitizenResponse updateCitizen(Long id, CitizenUpdateRequest request);

	CitizenResponse getCitizenById(Long id);

	List<CitizenResponse> getAll();

	void forgotPasswordC(String phone, String newPassword);

	// void forgotPassword(String phone, String newPassword);
}
