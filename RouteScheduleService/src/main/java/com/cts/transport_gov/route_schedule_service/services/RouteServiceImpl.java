package com.cts.transport_gov.route_schedule_service.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import com.cts.transport_gov.route_schedule_service.dtos.RouteCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.RouteResponse;
import com.cts.transport_gov.route_schedule_service.dtos.RouteUpdateRequest;
import com.cts.transport_gov.route_schedule_service.enums.RouteStatus;
import com.cts.transport_gov.route_schedule_service.exceptions.RouteNotFoundException;
import com.cts.transport_gov.route_schedule_service.models.Route;
import com.cts.transport_gov.route_schedule_service.repository.RouteRepository;


import lombok.RequiredArgsConstructor;

// Service implementation for managing Routes.
// Handles business logic and interacts with the repository layer.
// Loggers write messages when CRUD operations perform (like adding, updating, or deleting a route).

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements IRouteService {

	private static final Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);

	private final ModelMapper modelMapper; // Maps Models to DTOs, Eliminates Boilerplate.
	private final RouteRepository routeRepository;

	@Override // Add Route
	public RouteResponse addRoute(RouteCreateRequest route) {
		logger.info("Adding new route: {}", route.getTitle());
		Route savedRoute = routeRepository.save(modelMapper.map(route, Route.class));
		return modelMapper.map(savedRoute, RouteResponse.class);
	}

	@Override // Update Route
	public RouteResponse updateRoute(Long id, RouteUpdateRequest route) {
		logger.info("Updating route with id: {}", id);
		Route existing = routeRepository.findById(id)
				.orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + id));

		// Apply updates from DTO
		existing.setTitle(route.getTitle());
		existing.setType(route.getType());
		existing.setStartPoint(route.getStartPoint());
		existing.setEndPoint(route.getEndPoint());
		existing.setStatus(route.getStatus());

		Route updated = routeRepository.save(existing);
		logger.info("Route updated successfully with id: {}", updated.getRouteId());
		return modelMapper.map(updated, RouteResponse.class);
	}

	@Override // Get All Routes
	public List<RouteResponse> getAllRoutes() {
		logger.info("Fetching all routes");
		return routeRepository.findAll().stream().map(route -> modelMapper.map(route, RouteResponse.class)).toList();
	}

	@Override // Get Route by ID
	public RouteResponse getRouteById(Long id) {
		logger.info("Fetching route with id: {}", id);
		Route route = routeRepository.findById(id)
				.orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + id));
		return modelMapper.map(route, RouteResponse.class);
	}

	@Override // Delete Route
	public void deleteRoute(Long id) {
		logger.info("Deleting route with id: {}", id);
		if (!routeRepository.existsById(id)) {
			throw new RouteNotFoundException("Route not found with id: " + id);
		}
		routeRepository.deleteById(id);
		logger.info("Route deleted successfully with id: {}", id);
	}

	@Override // Get Routes by Type
	public List<RouteResponse> getRoutesByType(String type) {
		logger.info("Fetching routes by type: {}", type);
		return routeRepository.findByType(type).stream().map(route -> modelMapper.map(route, RouteResponse.class))
				.toList();
	}

	@Override
	public int countActiveRoutes() {
		return routeRepository.countByStatus(RouteStatus.ACTIVE);
	}
}
