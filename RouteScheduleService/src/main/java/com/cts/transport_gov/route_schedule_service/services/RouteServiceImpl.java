package com.cts.transport_gov.route_schedule_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.route_schedule_service.dtos.RouteCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.RouteResponse;
import com.cts.transport_gov.route_schedule_service.dtos.RouteUpdateRequest;
import com.cts.transport_gov.route_schedule_service.enums.RouteStatus;
import com.cts.transport_gov.route_schedule_service.exceptions.RouteNotFoundException;
import com.cts.transport_gov.route_schedule_service.models.Route;
import com.cts.transport_gov.route_schedule_service.repository.RouteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements IRouteService {

	private final RouteRepository routeRepository;

	@Override
	public int countActiveRoutes() {
		return (int) routeRepository.countByStatus(RouteStatus.INACTIVE);
	}

	@Override
	public RouteResponse addRoute(RouteCreateRequest request) {
		Route route = new Route();
		route.setTitle(request.getTitle());
		route.setType(request.getType());
		route.setStartPoint(request.getStartPoint());
		route.setEndPoint(request.getEndPoint());
		route.setStatus(request.getStatus());

		Route saved = routeRepository.save(route);
		return toResponse(saved);
	}

	@Override
	public RouteResponse updateRoute(Long id, RouteUpdateRequest request) {
		Route existing = routeRepository.findById(id)
				.orElseThrow(() -> new RouteNotFoundException("Route ID " + id + " not found"));

		existing.setTitle(request.getTitle());
		existing.setType(request.getType());
		existing.setStartPoint(request.getStartPoint());
		existing.setEndPoint(request.getEndPoint());
		existing.setStatus(request.getStatus());

		Route saved = routeRepository.save(existing);
		return toResponse(saved);
	}

	@Override
	public List<RouteResponse> getAllRoutes() {
		return routeRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
	}

	@Override
	public RouteResponse getRouteById(Long id) {
		Route route = routeRepository.findById(id)
				.orElseThrow(() -> new RouteNotFoundException("Route ID " + id + " not found"));
		return toResponse(route);
	}

	@Override
	public void deleteRoute(Long id) {
		if (!routeRepository.existsById(id)) {
			throw new RouteNotFoundException("Route ID " + id + " not found");
		}
		routeRepository.deleteById(id);
	}

	@Override
	public List<RouteResponse> getRoutesByType(String type) {
		return routeRepository.findByType(type).stream().map(this::toResponse).collect(Collectors.toList());
	}

	private RouteResponse toResponse(Route route) {
		RouteResponse response = new RouteResponse();
		response.setRouteId(route.getRouteId());
		response.setTitle(route.getTitle());
		response.setType(route.getType());
		response.setStartPoint(route.getStartPoint());
		response.setEndPoint(route.getEndPoint());
		response.setStatus(route.getStatus());
		return response;
	}
}
