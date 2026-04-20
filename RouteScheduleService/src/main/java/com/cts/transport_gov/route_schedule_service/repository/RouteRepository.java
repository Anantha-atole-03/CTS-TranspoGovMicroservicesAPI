package com.cts.transport_gov.route_schedule_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.route_schedule_service.enums.RouteStatus;
import com.cts.transport_gov.route_schedule_service.models.Route;

@Repository // create and manage the object
public interface RouteRepository extends JpaRepository<Route, Long> {
	List<Route> findByType(String type);

	List<Route> findByStatus(String status);

	int countByStatus(RouteStatus active);
}
