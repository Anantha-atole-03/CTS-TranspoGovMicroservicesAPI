package com.cts.transport_gov.authentication_service.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.model.User;
import com.cts.transport_gov.authentication_service.respository.CitizenRepository;
import com.cts.transport_gov.authentication_service.respository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of Spring Security's UserDetailsService. This class
 * acts as the bridge between the database and Spring Security, allowing the
 * application to load user data during the authentication process.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final CitizenRepository citizenRepository;

	/**
	 * Loads a user's full information from the database using their phone number as
	 * the username. This method supports a "Multi-Tenant" style lookup by checking
	 * both the Staff (User) and Citizen tables.
	 * 
	 * @param username The phone number provided during login.
	 * @return UserDetails An object that Spring Security uses for authentication
	 *         and authorization.
	 * @throws UsernameNotFoundException If the phone number does not exist in any
	 *                                   database table.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// 1. First, attempt to find the principal in the administrative/staff User
		// table
		Optional<User> user = userRepository.findByPhone(username);
		if (user.isPresent()) {
			// Database data is converted into UserDetails automatically because
			// the User model implements UserDetails.
			return user.get();
		}

		// 2. If not found in User, check the Citizen table
		// This ensures both Citizens and Staff can log in through the same security
		// filter.
		return citizenRepository.findByPhone(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid phone number or password"));
	}

}