package com.cts.transport_gov.authentication_service.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.dto.CitizenCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenResponse;
import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.exceptions.AuthenticationException;
import com.cts.transport_gov.authentication_service.model.Citizen;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import com.cts.transport_gov.authentication_service.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitizenService implements ICitizenService {
	private final CitizenRepository citizenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	@Override
	public CitizenResponse save(CitizenCreateRequest requestDto) {
		Optional<Citizen> exits = citizenRepository.findByPhone(requestDto.getPhone());
		if (exits.isPresent() || userRepository.findByPhone(requestDto.getPhone()).isPresent()) {
			throw new AuthenticationException("Citizen alredy exists");
		}
		Citizen user = modelMapper.map(requestDto, Citizen.class);
		user.setPhone(requestDto.getPhone());

		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		user.setRole(UserRole.CITIZEN_PASSENGER);
		Citizen user2 = citizenRepository.save(user);

		return modelMapper.map(user2, CitizenResponse.class);
	}

}
