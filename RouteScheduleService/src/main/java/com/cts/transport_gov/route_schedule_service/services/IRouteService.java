package com.cts.transport_gov.route_schedule_service.services;

import java.util.List;

import com.cts.transport_gov.route_schedule_service.dtos.RouteCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.RouteResponse;
import com.cts.transport_gov.route_schedule_service.dtos.RouteUpdateRequest;

public interface IRouteService {
	int countActiveRoutes();

	RouteResponse addRoute(RouteCreateRequest route);

	RouteResponse updateRoute(Long id, RouteUpdateRequest route);

	List<RouteResponse> getAllRoutes();

	RouteResponse getRouteById(Long id);

	void deleteRoute(Long id);

	List<RouteResponse> getRoutesByType(String type);
}
