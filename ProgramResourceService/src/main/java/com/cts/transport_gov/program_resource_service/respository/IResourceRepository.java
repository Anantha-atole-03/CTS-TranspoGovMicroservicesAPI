package com.cts.transport_gov.program_resource_service.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.program_resource_service.model.Resource;

@Repository
public interface IResourceRepository extends JpaRepository<Resource, Long> {
	List<Resource> findByProgramProgramId(Long programId);

	@Query("SELECT COALESCE(SUM(r.budget), 0) FROM Resource r WHERE r.program.programId = :programId")
	Double getTotalBudgetByProgramId(@Param("programId") Long programId);

}
