package com.cts.transport_gov.authentication_service.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.transport_gov.authentication_service.model.Citizen;

public interface CitizenDocumentRepository extends JpaRepository<Citizen, Long> {

}
