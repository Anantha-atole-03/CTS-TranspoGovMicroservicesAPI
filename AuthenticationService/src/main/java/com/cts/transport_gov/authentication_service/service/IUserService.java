package com.cts.transport_gov.authentication_service.service;

import java.util.List;

import com.cts.transport_gov.authentication_service.dto.UserCreateRequest;
import com.cts.transport_gov.authentication_service.dto.UserResponse;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.model.User;

public interface IUserService {
	UserResponse save(UserCreateRequest requestDto);

	List<User> getAllUsers();

	void updateUser(User user, Long userId);

	void updateUserRoles(UserRole userRole, Long userId);

	User findById(Long id);

	UserResponse findByEmail(String email);
}
