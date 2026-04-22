package com.cts.transport_gov.authentication_service.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.dto.UserCreateRequest;
import com.cts.transport_gov.authentication_service.dto.UserResponse;
import com.cts.transport_gov.authentication_service.exceptions.AuthenticationException;
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

	@Override
	public UserResponse save(UserCreateRequest requestDto) {
		Optional<User> exits = userRepository.findByPhone(requestDto.getPhone());
		if (exits.isPresent() || citizenRepository.findByPhone(requestDto.getPhone()).isPresent()) {
			throw new AuthenticationException("User alredy exists");
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

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public static String generateSixDigitPassword() {
		SecureRandom random = new SecureRandom();

		int number = 100000 + random.nextInt(900000);

		return String.valueOf(number);
	}

}
