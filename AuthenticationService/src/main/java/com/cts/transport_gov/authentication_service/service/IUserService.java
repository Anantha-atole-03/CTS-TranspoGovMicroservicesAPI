package com.cts.transport_gov.authentication_service.service;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.cts.transport_gov.authentication_service.dto.UserCreateRequest;
import com.cts.transport_gov.authentication_service.dto.UserResponse;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.enums.UserStatus;
import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.model.User;

public interface IUserService {
	UserResponse save(UserCreateRequest requestDto);

	List<User> getAllUsers();

	void updateUser(User user, Long userId);

	void updateUserRoles(UserRole userRole, Long userId);

	UserResponse findByEmail(String email);

	UserResponse findById(Long id);
	

	List<AuditLog> getAllLogs(Long adminId);
	public String approveUser(Long adminId,String status, Long userId);
	List<User>getAll();
	List<User> findByPending();
}
