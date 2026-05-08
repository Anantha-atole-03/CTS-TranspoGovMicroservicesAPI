package com.cts.transport_gov.authentication_service.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.authentication_service.enums.UserRole;
import com.cts.transport_gov.authentication_service.enums.UserStatus;
import com.cts.transport_gov.authentication_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByPhone(String phone);

	Optional<User> findByEmail(String email);
	// void forgotPassword(ForgotPasswordRequest request);

	boolean existsByRole(UserRole administrator);
	List<User> findByStatus(UserStatus status);

}
