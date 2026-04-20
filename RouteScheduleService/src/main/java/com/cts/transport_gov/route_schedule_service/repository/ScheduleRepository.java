package com.cts.transport_gov.route_schedule_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.route_schedule_service.models.Schedule;

import java.util.List;

@Repository // create and manage the object
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	List<Schedule> findByRoute_RouteId(Long routeId);

	List<Schedule> findByStatus(String status);
}
