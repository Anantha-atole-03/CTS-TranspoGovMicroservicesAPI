package com.cts.transport_gov.authentication_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.authentication_service.dto.UserCreateRequest;
import com.cts.transport_gov.authentication_service.dto.UserResponse;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for managing User accounts and authentication details.
 * Handles user registration, profile updates, and role management.
 */
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final IUserService userService;

	/**
	 * Registers a new user in the system. * @param user DTO containing the initial
	 * user details (username, password, etc.).
	 * 
	 * @return ResponseEntity containing the created User object and HTTP 201
	 *         Created status.
	 */
	@PostMapping("/")
	public ResponseEntity<UserResponse> addUser(@RequestBody UserCreateRequest user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
	}

	/**
	 * Retrieves a list of all users registered in the system. * @return List of all
	 * User entities.
	 */
	@GetMapping("/")
	public List<User> allUsers() {
		return userService.getAllUsers();
	}

	/**
	 * Updates an existing user's profile information. * @param user The user object
	 * containing updated fields.
	 * 
	 * @param userId The unique ID of the user to be updated.
	 */
	@PutMapping("/{userId}")
	public void updateUser(@RequestBody User user, @PathVariable Long userId) {
		userService.updateUser(user, userId);
		System.out.println("User updated successfully");
	}

	/**
	 * Updates the specific role/authority of a user. * @param userRole The new role
	 * to assign (e.g., ADMIN, CITIZEN).
	 * 
	 * @param userId The unique ID of the user.
	 */
	@PutMapping("/{userId}/role")
	public void updateRole(@RequestBody UserRole userRole, @PathVariable Long userId) {
		userService.updateUserRoles(userRole, userId);
	}

	/**
	 * Fetches the details of a specific user by their unique identifier. * @param
	 * id The unique ID of the user.
	 * 
	 * @return The User entity matching the provided ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getuser(@PathVariable Long id) {
		return ResponseEntity.ok(userService.findById(id));
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<UserResponse> getuser(@PathVariable String email) {
		return ResponseEntity.ok(userService.findByEmail(email));

	}
	@GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getLogs(@RequestHeader("admin-id") Long adminId) {
        log.info("Audit logs requested by admin ID: {}", adminId);
        return ResponseEntity.ok(userService.getAllLogs(adminId));
    }
}