package com.cts.transport_gov.authentication_service.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.dto.LoginRequestDto;
import com.cts.transport_gov.authentication_service.dto.LoginResponseDto;
import com.cts.transport_gov.authentication_service.model.Citizen;
import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import com.cts.transport_gov.authentication_service.respository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for authenticating users and citizens. It validates
 * credentials via Spring Security and generates JWT access tokens.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final AuthenticationManager authenticationManager;
	private final AuthUtils authUtils;
	private final UserRepository userRepository;
	private final CitizenRepository citizenRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Authenticates the user based on phone and password, then determines the
	 * account type to build the login response.
	 * 
	 * @param loginRequestDto Contains login credentials (phone and password).
	 * @return LoginResponseDto containing the JWT token, phone, and assigned role.
	 */
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		// 1. Trigger the authentication process using the phone as the username
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDto.getPhone(), loginRequestDto.getPassword()));

		LoginResponseDto loginResponseDto = new LoginResponseDto();

		/*
		 * 2. Identify the Principal type. Since our system supports both 'User'
		 * (Admin/Staff) and 'Citizen' entities, we check which instance was returned by
		 * the AuthenticationManager.
		 */
		if (authentication.getPrincipal() instanceof User user) {
			// Case: Principal is a Staff/Admin User
			loginResponseDto.setPhone(user.getPhone());
			loginResponseDto.setRole(user.getRole());
		} else {
			// Case: Principal is a Citizen
			Citizen citizen = (Citizen) authentication.getPrincipal();
			loginResponseDto.setPhone(citizen.getPhone());
			loginResponseDto.setRole(citizen.getRole());
		}

		/*
		 * NOTE ON GETPRINCIPAL(): This allows me to cast the Principal correctly and
		 * extract either a userId or a citizenId to put into the JWT claims. You can
		 * grab the result anywhere. Usually contains one of two things: - A String:
		 * username or userId extracted from the JWT "sub" claim. - A Custom Object: A
		 * class implementing UserDetails (like a User entity) that contains the
		 * username, database ID, and email. Use getPrincipal() when your business logic
		 * needs to know who is calling the API.
		 */

		// 3. Generate a JWT token based on the UserDetails of the authenticated
		// principal
		String token = authUtils.generateAccessToken((UserDetails) authentication.getPrincipal());
		loginResponseDto.setToken(token);

		return loginResponseDto;
	}

	/**
	 * COMMAND: Generate a unique recovery token and initiate password reset
	 * protocol.
	 */

	@Transactional
	public String forgotPassword(String email, String newPassword) {
		log.info("Executing forgot-password command for: {}", email);
		Optional<Citizen> citizen = citizenRepository.findByEmail(email);
		if (!citizen.isPresent()) {
			User user = userRepository.findByEmail(email).orElseThrow(() -> {
				log.warn("Forgot Password: Email {} not found. Triggering generic response.", email);
				return new RuntimeException("Email invalid");
			});
			user.setPassword(passwordEncoder.encode(newPassword));
			return "Password set successfully";
		}

		citizen.get().setPassword(passwordEncoder.encode(newPassword));

		log.info("Passeord set: Email sent to {} ", email);

		return "Password set successfully";
	}
}