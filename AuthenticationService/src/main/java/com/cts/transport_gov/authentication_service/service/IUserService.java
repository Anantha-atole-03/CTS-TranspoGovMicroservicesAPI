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

<<<<<<< HEAD
	User findById(Long id);

	UserResponse findByEmail(String email);
=======
	UserResponse findById(Long id);
>>>>>>> e8f5c21746fcf37cda7be901d892053f08e46834
}
