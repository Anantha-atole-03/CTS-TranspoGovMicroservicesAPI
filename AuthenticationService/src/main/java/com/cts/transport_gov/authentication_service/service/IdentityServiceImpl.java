package com.cts.transport_gov.authentication_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.authentication_service.config.JwtUtil;
import com.cts.transport_gov.authentication_service.dtos.AuthRequest;
import com.cts.transport_gov.authentication_service.dtos.AuthResponse;
import com.cts.transport_gov.authentication_service.dtos.ResetPasswordReq;
import com.cts.transport_gov.authentication_service.dtos.UserRegistrationReq;
import com.cts.transport_gov.authentication_service.dtos.UserResponse;
import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.model.PasswordResetToken;
import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.model.UserRole;
import com.cts.transport_gov.authentication_service.model.UserStatus;
import com.cts.transport_gov.authentication_service.repository.AuditLogRepository;
import com.cts.transport_gov.authentication_service.repository.PasswordResetTokenRepository;
import com.cts.transport_gov.authentication_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityServiceImpl implements IdentityService {

	private final UserRepository userRepository;
	private final AuditLogRepository auditLogRepository; 
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final PasswordResetTokenRepository tokenRepo;

	/**
	 * COMMAND: Register a new user with encrypted credentials and initialize an audit trail.
	 */
	@Override
	@Transactional
	public UserResponse registerUser(UserRegistrationReq req) {
		log.info("Processing registration command for email: {}", req.getEmail());

		User user = new User();
		user.setName(req.getName());
		user.setEmail(req.getEmail());
		user.setPhone(req.getPhone());
		user.setPassword(passwordEncoder.encode(req.getPassword()));
		user.setRole(req.getRole());
		user.setStatus(UserStatus.ACTIVE);

		User savedUser = userRepository.save(user);

		// Initialize Audit Trail
		AuditLog logEntry = new AuditLog();
		logEntry.setUserId(savedUser.getUserId());
		logEntry.setAction("REGISTER_USER");
		logEntry.setResource("IDENTITY");
		logEntry.setTimestamp(LocalDateTime.now());
		auditLogRepository.save(logEntry);

		log.info("Registration command completed for User ID: {}", savedUser.getUserId());
		return mapToResponse(savedUser);
	}

	/**
	 * COMMAND: Validate credentials, enforce account status checks, and generate access tokens.
	 */
	@Override
	public AuthResponse authenticate(AuthRequest req) {
		log.info("Executing authentication command for user: {}", req.getEmail());

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

		User user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> {
			log.error("Auth Failure: Email {} not found in system record.", req.getEmail());
			return new RuntimeException("User not found");
		});

		if (user.getStatus().name().equals("INACTIVE")) {
			log.warn("Access Denied: Account {} is currently INACTIVE/SUSPENDED.", req.getEmail());
			throw new RuntimeException("Access Denied: Your account has been deactivated by an Administrator.");
		}

		logAction(user, "LOGIN_SUCCESS", "IDENTITY");
		String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

		log.info("Authentication command successful for user: {}", req.getEmail());
		return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getStatus().name(),
				"Login Successful");
	}

	/**
	 * COMMAND: Asynchronously or synchronously persist system-wide audit logs.
	 */
	@Override
	@Transactional
	public void logAction(User user, String action, String resource) {
		log.debug("Command: Logging action [{}] for user [{}]", action, user.getEmail());
		AuditLog logEntry = new AuditLog();
		logEntry.setUserId(user.getUserId());
		logEntry.setAction(action);
		logEntry.setResource(resource);
		logEntry.setTimestamp(LocalDateTime.now());
		auditLogRepository.save(logEntry);
	}

	/**
	 * COMMAND: Retrieve all audit logs with mandatory Administrator privilege check.
	 */
	@Override
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

	/**
	 * COMMAND: Set user status to INACTIVE to restrict system access.
	 */
	@Override
	public String suspendUser(String email) {
		log.info("Executing suspension command for: {}", email);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));

		user.setStatus(UserStatus.INACTIVE);
		userRepository.save(user);
		logAction(user, "ACCOUNT_SUSPENDED", "IDENTITY");

		log.info("Suspension command finalized for email: {}", email);
		return "User " + email + " has been successfully suspended.";
	}

	/**
	 * COMMAND: Set user status to ACTIVE to restore system access.
	 */
	@Override
	public String reactivateUser(String email) {
		log.info("Executing reactivation command for: {}", email);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));

		if (user.getStatus().name().equals("ACTIVE")) {
			log.info("Reactivation command skipped: {} is already ACTIVE.", email);
			return "User " + email + " is already active.";
		}

		user.setStatus(UserStatus.ACTIVE);
		userRepository.save(user);
		logAction(user, "ACCOUNT_REACTIVATED", "IDENTITY");

		log.info("Reactivation command finalized for email: {}", email);
		return "User " + email + " has been successfully reactivated. They can now log in.";
	}

	/**
	 * COMMAND: Generate a unique recovery token and initiate password reset protocol.
	 */
	@Override
	@Transactional
	public String forgotPassword(String email) {
		log.info("Executing forgot-password command for: {}", email);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					log.warn("Forgot Password: Email {} not found. Triggering generic response.", email);
					return new RuntimeException("If this email exists, a reset link will be sent.");
				});

		String token = java.util.UUID.randomUUID().toString();

		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUserId(user.getUserId());
		resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
		tokenRepo.save(resetToken);

		log.info("MOCK EMAIL SENDER: Email sent to {} with reset token: {}", email, token);

		return "If an account matches that email, a password reset token has been sent.";
	}

	/**
	 * COMMAND: Validate token integrity and update user credentials.
	 */
	@Override
	@Transactional
	public String resetPassword(ResetPasswordReq req) {
		log.info("Executing password-reset command using token.");

		PasswordResetToken resetToken = tokenRepo.findByToken(req.getResetToken())
				.orElseThrow(() -> {
					log.error("Password Reset Failed: Provided token was invalid or missing.");
					return new RuntimeException("Invalid or missing token.");
				});

		if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			log.warn("Password Reset Failed: Token for User ID {} has expired.", resetToken.getUserId());
			tokenRepo.delete(resetToken);
			throw new RuntimeException("Token has expired. Please request a new one.");
		}

		User user = userRepository.findById(resetToken.getUserId())
				.orElseThrow(() -> new RuntimeException("User associated with token not found."));

		user.setPassword(passwordEncoder.encode(req.getNewPassword()));
		userRepository.save(user);

		tokenRepo.delete(resetToken);
		logAction(user, "PASSWORD_RESET_SUCCESS", "IDENTITY");

		log.info("Password reset command completed for user: {}", user.getEmail());
		return "Your password has been successfully reset. You may now log in.";
	}
	
	/**
	 * COMMAND: Fetch user details by ID.
	 */
	public UserResponse getUserById(Long userId) {
		log.info("Fetching details for User ID: {}", userId);
		User user = userRepository.findById(userId).orElseThrow(() -> {
			log.error("Get User Failed: ID {} not found.", userId);
			return new RuntimeException("User not found");
		});

		return new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole().name(),
				user.getStatus().name());
	}
	
	/**
	 * Private helper to map Entity to DTO.
	 */
	private UserResponse mapToResponse(User user) {
		UserResponse res = new UserResponse();
		res.setUserId(user.getUserId());
		res.setName(user.getName());
		res.setEmail(user.getEmail());
		res.setRole(user.getRole().name());
		res.setStatus(user.getStatus().name());
		return res;
	}
}