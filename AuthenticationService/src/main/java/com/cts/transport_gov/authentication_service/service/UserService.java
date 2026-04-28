package com.cts.transport_gov.authentication_service.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.dto.UserCreateRequest;
import com.cts.transport_gov.authentication_service.dto.UserResponse;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.exceptions.AuthenticationException;
import com.cts.transport_gov.authentication_service.exceptions.InvalidDataException;
import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import com.cts.transport_gov.authentication_service.respository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
	private final UserRepository userRepository;
	private final CitizenRepository citizenRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	/**
	 * Converts a request DTO into a User entity and persists it.
	 * 
	 * @param request The user creation details.
	 * @return The newly created User entity.
	 * @throws IllegalStateException if the mapping process fails.
	 */

	@Override
	public UserResponse save(UserCreateRequest requestDto) {
		Optional<User> exits = userRepository.findByPhone(requestDto.getPhone());
		if (exits.isPresent() || citizenRepository.findByPhone(requestDto.getPhone()).isPresent()) {
			throw new AuthenticationException("User alredy exists");
		}

		if (requestDto.getRole().equals(UserRole.CITIZEN_PASSENGER)) {
			throw new InvalidDataException("Invalid user role! provide currect data");
		}
		User user = modelMapper.map(requestDto, User.class);

		String password = generateSixDigitPassword();
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(requestDto.getRole());
		User user2 = userRepository.save(user);
		log.warn("Password: {} for user:{}", requestDto.getPhone(), password);
		// TODO: call send email logic
		return modelMapper.map(user2, UserResponse.class);
	}

	/**
	 * Retrieves all users currently stored in the system.
	 * 
	 * @return A list of all User entities.
	 */
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public static String generateSixDigitPassword() {
		SecureRandom random = new SecureRandom();

		int number = 100000 + random.nextInt(900000);

		return String.valueOf(number);
	}

	/**
	 * Updates an existing user's profile by merging new data into the existing
	 * record.
	 * 
	 * @param user   The object containing updated data.
	 * @param userId The ID of the user to update.
	 * @throws IllegalArgumentException if ID is invalid.
	 * @throws RuntimeException         if the user is not found.
	 */
	@Override
	public void updateUser(User user, Long userId) {
		log.info("Attempting to update user details for ID: {}", userId);

		if (userId == null || userId <= 0) {
			log.warn("Update failed: Invalid userId provided: {}", userId);
			throw new IllegalArgumentException("Invalid user UserId");
		}

		User existingUser = userRepository.findById(userId).orElseThrow(() -> {
			log.error("Update failed: User with ID {} not found", userId);
			return new RuntimeException("User not found");
		});

		// Merges non-null fields from 'user' into 'existingUser'
		modelMapper.map(user, existingUser);

		// Ensure the ID remains consistent during the merge
		existingUser.setUserId(userId);

		userRepository.save(existingUser);
		log.info("User with ID: {} updated successfully", userId);
	}

	/**
	 * Specifically updates the administrative or access role of a user.
	 * 
	 * @param userRole The new role to assign.
	 * @param userId   The ID of the target user.
	 */
	@Override
	public void updateUserRoles(UserRole userRole, Long userId) {
		log.info("Updating role to {} for user ID: {}", userRole, userId);

		if (userId == null || userRole == null) {
			log.warn("Role update failed: userId or userRole is null");
			throw new IllegalArgumentException("Invalid user UserId or UserRole");
		}

		User user = userRepository.findById(userId).orElseThrow(() -> {
			log.error("Role update failed: User {} not found", userId);
			return new RuntimeException("User not found");
		});

		user.setRole(userRole);
		userRepository.save(user);
		log.info("Role updated successfully for user ID: {}", userId);
	}

	/**
	 * Finds a single user by their primary key.
	 * 
	 * @param id The user ID.
	 * @return The User entity if found, or null if not present.
	 */
	@Override
	public UserResponse findById(Long id) {
		log.debug("Finding user by ID: {}", id);
		if (id == null) {
			log.warn("findById called with null ID");
			throw new IllegalArgumentException("Invalid user ID");
		}
		return modelMapper.map(userRepository.findById(id).orElse(null), UserResponse.class);
	}

}
