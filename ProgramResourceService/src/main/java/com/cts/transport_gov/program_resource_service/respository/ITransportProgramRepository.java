package com.cts.transport_gov.program_resource_service.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.program_resource_service.model.TransportProgram;

@Repository
public interface ITransportProgramRepository extends JpaRepository<TransportProgram, Long> {

}
