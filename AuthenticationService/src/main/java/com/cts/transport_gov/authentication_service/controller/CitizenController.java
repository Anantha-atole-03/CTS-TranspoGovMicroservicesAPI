package com.cts.transport_gov.authentication_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.authentication_service.dto.CitizenCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenResponse;
import com.cts.transport_gov.authentication_service.dto.CitizenUpdateRequest;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.service.ICitizenService;
import com.cts.transport_gov.authentication_service.service.IUserService;

import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Citizen profiles and account roles. Provides
 * endpoints for CRUD operations on citizens and administrative role updates.
 */
@RestController
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class CitizenController {

	private final ICitizenService citizenServiceImpl;
	private final IUserService userService;

	/**
	 * Creates a new citizen record in the system. * @param request DTO containing
	 * registration details.
	 * 
	 * @return CitizenResponse containing the persisted data and generated ID.
	 */
	@PostMapping
	public CitizenResponse createCitizen(@RequestBody CitizenCreateRequest request) {
		return citizenServiceImpl.addCitizen(request);
	}

	/**
	 * Updates the profile information of an existing citizen. * @param id The
	 * unique identifier of the citizen.
	 * 
	 * @param request DTO containing updated profile fields.
	 * @return CitizenResponse with the updated information.
	 */
	@PutMapping("/{id}")
	public CitizenResponse updateCitizen(@PathVariable Long id, @RequestBody CitizenUpdateRequest request) {
		return citizenServiceImpl.updateCitizen(id, request);
	}

	/**
	 * Fetches details of a specific citizen by their ID. * @param id The unique
	 * identifier of the citizen.
	 * 
	 * @return CitizenResponse object.
	 */
	@GetMapping("/{id}")
	public CitizenResponse getCitizen(@PathVariable Long id) {
		return citizenServiceImpl.getCitizenById(id);
	}

	/**
	 * Retrieves a list of all registered citizens in the system. * @return List of
	 * CitizenResponse objects.
	 */
	@GetMapping
	public List<CitizenResponse> getAllCitizens() {
		return citizenServiceImpl.getAll();
	}

	/**
	 * Administrative endpoint to modify a user's access level/role. * @param
	 * userRole The new UserRole to be assigned (e.g., ADMIN, CITIZEN).
	 * 
	 * @param userId The ID of the user whose role is being modified.
	 */
	@PutMapping("/{userId}/role")
	public void updateRole(@RequestBody UserRole userRole, @PathVariable Long userId) {
		userService.updateUserRoles(userRole, userId);
	}

}