package com.cts.transport_gov.authentication_service.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.client.NotificationServiceClient;
import com.cts.transport_gov.authentication_service.dto.OtpNotificationRequest;
import com.cts.transport_gov.authentication_service.dto.UserCreateRequest;
import com.cts.transport_gov.authentication_service.dto.UserResponse;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.exceptions.AuthenticationException;
import com.cts.transport_gov.authentication_service.exceptions.InvalidDataException;
import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.respository.AuditLogRepository;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import com.cts.transport_gov.authentication_service.respository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final CitizenRepository citizenRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuditLogRepository auditLogRepository;
	private final ModelMapper modelMapper;
	private final NotificationServiceClient client;

	// ✅ CORRECT injection
	private final AuditLogService auditLogService;

	@Override
	public UserResponse save(UserCreateRequest requestDto) {

		Optional<User> exists = userRepository.findByPhone(requestDto.getPhone());
		if (exists.isPresent() || citizenRepository.findByPhone(requestDto.getPhone()).isPresent()) {
			throw new AuthenticationException("User already exists");
		}

		if (requestDto.getRole().equals(UserRole.CITIZEN_PASSENGER)) {
			throw new InvalidDataException("Invalid user role! provide correct data");
		}

		User user = modelMapper.map(requestDto, User.class);

		String password = generateSixDigitPassword();
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(requestDto.getRole());

		User savedUser = userRepository.save(user);

		log.warn("Password: {} for user: {}", requestDto.getPhone(), password);

		OtpNotificationRequest otp = new OtpNotificationRequest();
		otp.setEmail(requestDto.getEmail());
		otp.setOtp(password);

		try {
			client.sendOtpNotification(otp);
		} catch (Exception e) {
			log.error("OTP failed, continuing signup", e);
		}

		// ✅ AUDIT LOG
		auditLogService.logAction(savedUser.getUserId(), "REGISTER_USER", "IDENTITY");

		return modelMapper.map(savedUser, UserResponse.class);
	}

	@Override
	public void updateUser(User user, Long userId) {

		User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		modelMapper.map(user, existingUser);
		existingUser.setUserId(userId);
		userRepository.save(existingUser);

		// ✅ AUDIT LOG
		auditLogService.logAction(userId, "UPDATE_USER_PROFILE", "IDENTITY");
	}

	@Override
	public void updateUserRoles(UserRole userRole, Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		user.setRole(userRole);
		userRepository.save(user);

		// ✅ AUDIT LOG

		auditLogService.logAction(user.getUserId(), "UPDATE_USER_ROLE_" + userRole.name(), "IDENTITY");

	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public UserResponse findById(Long id) {
		return modelMapper.map(userRepository.findById(id).orElse(null), UserResponse.class);
	}

	@Override
	public UserResponse findByEmail(String email) {
		return modelMapper.map(userRepository.findByEmail(email).orElse(null), UserResponse.class);
	}

	public static String generateSixDigitPassword() {
		SecureRandom random = new SecureRandom();
		return String.valueOf(100000 + random.nextInt(900000));
	}

	@Override
	@Transactional
	public List<AuditLog> getAllLogs(Long adminId) {
		log.info("Executing log retrieval command requested by User ID: {}", adminId);

		User admin = userRepository.findById(adminId).orElseThrow(() -> {
			log.error("Log Retrieval Failed: Requester ID {} not found.", adminId);
			return new RuntimeException("User not found");
		});

		if (admin.getRole() != UserRole.ADMINISTRATOR) {
			log.error("Security Violation: User ID {} lacks ADMINISTRATOR privileges.", adminId);
			throw new RuntimeException("Access Denied: Admin role required");
		}

		log.info("Log retrieval command executed successfully for Admin ID: {}", adminId);
		return auditLogRepository.findAll();
	}
}