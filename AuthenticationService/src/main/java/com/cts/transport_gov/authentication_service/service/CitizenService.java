package com.cts.transport_gov.authentication_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.authentication_service.dto.CitizenCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenResponse;
import com.cts.transport_gov.authentication_service.dto.CitizenUpdateRequest;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.exceptions.AuthenticationException;
import com.cts.transport_gov.authentication_service.exceptions.UserAlreadyExistsExcetions;
import com.cts.transport_gov.authentication_service.model.Citizen;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import com.cts.transport_gov.authentication_service.respository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for managing Citizen lifecycle and profile operations.
 * Handles registration, profile updates, and secure password management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CitizenService implements ICitizenService {

	private final CitizenRepository citizenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;

	/**
	 * Registers a new citizen after verifying the phone number uniqueness.
	 * 
	 * @param request Data containing the citizen's registration information.
	 * @return CitizenResponse containing the newly created record details.
	 * @throws UserAlreadyExistsExcetions if the phone number is already in use by a
	 *                                    citizen.
	 */
	@Override
	public CitizenResponse addCitizen(CitizenCreateRequest request) {
		log.info("Registering new citizen with phone: {}", request.getPhone());

		// Ensure no duplicate account exists for this phone number
		citizenRepository.findByPhone(request.getPhone()).ifPresent(c -> {
			throw new UserAlreadyExistsExcetions("Account with phone " + request.getPhone() + " already exists.");
		});

		// Map DTO to Entity and persist
		Citizen citizen = mapper.map(request, Citizen.class);
		Citizen savedCitizen = citizenRepository.save(citizen);

		return mapper.map(savedCitizen, CitizenResponse.class);
	}

	/**
	 * Updates an existing citizen's profile details.
	 * 
	 * @param id      The primary key of the citizen.
	 * @param request The updated data.
	 * @return Updated CitizenResponse.
	 * @throws java.util.NoSuchElementException if ID is not found.
	 */
	@Override
	public CitizenResponse updateCitizen(Long id, CitizenUpdateRequest request) {
		log.info("Updating citizen ID: {}", id);

		// Retrieve existing record or throw exception
		Citizen existingCitizen = citizenRepository.findById(id).orElseThrow();

		// Merge updated fields into the existing entity
		mapper.map(request, existingCitizen);

		Citizen updatedCitizen = citizenRepository.save(existingCitizen);
		return mapper.map(updatedCitizen, CitizenResponse.class);
	}

	/**
	 * Retrieves a single citizen's profile by ID.
	 * 
	 * @param id The citizen ID.
	 * @return CitizenResponse.
	 */
	@Override
	public CitizenResponse getCitizenById(Long id) {
		Citizen citizen = citizenRepository.findById(id).orElseThrow();
		return mapper.map(citizen, CitizenResponse.class);
	}

	/**
	 * Fetches all registered citizens from the system.
	 * 
	 * @return List of CitizenResponse DTOs.
	 */
	@Override
	public List<CitizenResponse> getAll() {
		return citizenRepository.findAll().stream().map(citizen -> mapper.map(citizen, CitizenResponse.class))
				.collect(Collectors.toList());
	}

	/**
	 * Securely saves a citizen with an encrypted password and default role. Checks
	 * both Citizen and User tables to prevent cross-account duplicates.
	 * 
	 * @param requestDto DTO containing citizen details including password.
	 * @return CitizenResponse.
	 * @throws AuthenticationException if the phone number exists in any user table.
	 */
	@Override
	public CitizenResponse save(CitizenCreateRequest requestDto) {
		// Global check across repositories to prevent duplicate phone numbers
		Optional<Citizen> exits = citizenRepository.findByPhone(requestDto.getPhone());
		if (exits.isPresent() || userRepository.findByPhone(requestDto.getPhone()).isPresent()) {
			throw new AuthenticationException("Citizen alredy exists");
		}

		Citizen user = mapper.map(requestDto, Citizen.class);
		user.setPhone(requestDto.getPhone());

		// Encrypt the plain-text password before saving
		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

		// Assign the default role for registered passengers
		user.setRole(UserRole.CITIZEN_PASSENGER);

		Citizen user2 = citizenRepository.save(user);

		return mapper.map(user2, CitizenResponse.class);
	}

	@Override
	public void forgotPasswordC(String phone, String newPassword) {

		String encodedPassword = passwordEncoder.encode(newPassword);

		// Update Citizen password
		citizenRepository.findByPhone(phone).ifPresent(citizen -> {
			citizen.setPassword(encodedPassword);
			citizenRepository.save(citizen);
		});

		// Update User password (VERY IMPORTANT)
		userRepository.findByPhone(phone).ifPresent(user -> {
			user.setPassword(encodedPassword);
			userRepository.save(user);
		});
	}

	@Override
	public CitizenResponse getCitizenByEmail(String email) {
		Citizen citizen = citizenRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Citizen not found with email: " + email));
		return mapper.map(citizen, CitizenResponse.class);
	}

}