package com.cts.transport_gov.authentication_service.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.authentication_service.model.Citizen;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
	Optional<Citizen> findByPhone(String phone);
}
