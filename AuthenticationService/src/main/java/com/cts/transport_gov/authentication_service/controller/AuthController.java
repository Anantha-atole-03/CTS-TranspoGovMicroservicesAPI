package com.cts.transport_gov.authentication_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.authentication_service.dto.CitizenCreateRequest;
import com.cts.transport_gov.authentication_service.dto.CitizenResponse;
import com.cts.transport_gov.authentication_service.dto.LoginRequestDto;
import com.cts.transport_gov.authentication_service.dto.LoginResponseDto;
import com.cts.transport_gov.authentication_service.dto.UserCreateRequest;
import com.cts.transport_gov.authentication_service.dto.UserResponse;
import com.cts.transport_gov.authentication_service.security.AuthService;
import com.cts.transport_gov.authentication_service.service.ICitizenService;
import com.cts.transport_gov.authentication_service.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final IUserService userService;
	private final ICitizenService citizenService;

	@PostMapping("/user/signup")
	public ResponseEntity<UserResponse> userSignup(@RequestBody UserCreateRequest requestDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(requestDto));
	}

	@PostMapping("/citizen/signup")
	public ResponseEntity<CitizenResponse> citizenSignup(@RequestBody CitizenCreateRequest dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(citizenService.save(dto));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
		return ResponseEntity.ok(authService.login(loginRequestDto));
	}
}
