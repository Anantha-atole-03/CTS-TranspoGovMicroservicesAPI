package com.cts.transport_gov.authentication_service.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.authentication_service.enums.UserStatus;
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

	    // ✅ 1. Check USER table (staff)
	    Optional<User> optionalUser = userRepository.findByPhone(username);

	    if (optionalUser.isPresent()) {

	        User user = optionalUser.get();

	        // ✅ ✅ APPLY STATUS CHECK ONLY FOR USERS
	        if (user.getStatus() == UserStatus.PENDING) {
	            throw new RuntimeException("User not approved by ADMIN");
	        }

	        if (user.getStatus() == UserStatus.REJECT) {
	            throw new RuntimeException("User account is suspended");
	        }

	        if (user.getStatus() == UserStatus.DELETED) {
	            throw new RuntimeException("User account is deleted");
	        }

	        if (user.getStatus() == UserStatus.INACTIVE) {
	            throw new RuntimeException("User account is inactive");
	        }

	        return user; 
	    }

	    // ✅ 2. Check CITIZEN table
	    // 🔥 NO STATUS CHECK HERE → citizen login directly allowed
	    return citizenRepository.findByPhone(username)
	            .orElseThrow(() ->
	                    new UsernameNotFoundException("Invalid phone number or password"));
	}
}