package com.cts.transport_gov.authentication_service.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.authentication_service.model.CitizenDocument;

@Repository
public interface CitizenDocumentRepository extends JpaRepository<CitizenDocument, Long> {
	List<CitizenDocument> findByCitizen_CitizenId(Long citizenId);

}
