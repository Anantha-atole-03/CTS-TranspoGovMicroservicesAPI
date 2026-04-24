package com.cts.transport_gov.route_schedule_service.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.route_schedule_service.dtos.RouteCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.RouteNotificationRequest;
import com.cts.transport_gov.route_schedule_service.dtos.RouteResponse;
import com.cts.transport_gov.route_schedule_service.dtos.RouteUpdateRequest;
import com.cts.transport_gov.route_schedule_service.enums.RouteStatus;
import com.cts.transport_gov.route_schedule_service.exceptions.RouteNotFoundException;
import com.cts.transport_gov.route_schedule_service.feign_client.NotificationClient;
import com.cts.transport_gov.route_schedule_service.models.Route;
import com.cts.transport_gov.route_schedule_service.repository.RouteRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteServiceImpl implements IRouteService {

	private final ModelMapper modelMapper;
	private final RouteRepository routeRepository;
	private final NotificationClient notificationClient; // Feign client injected

	@Override
	public RouteResponse addRoute(RouteCreateRequest routeRequest) {
		Route route = modelMapper.map(routeRequest, Route.class);
		Route saved = routeRepository.save(route);

		// Send notification after saving the route
		RouteNotificationRequest notificationRequest = new RouteNotificationRequest(routeRequest.getEmail(),
				saved.getTitle());
//		notificationClient.sendRouteUpdateNotification(notificationRequest);

		return modelMapper.map(saved, RouteResponse.class);
	}

	@Override
	public RouteResponse updateRoute(Long id, RouteUpdateRequest routeRequest) {
		Route existing = routeRepository.findById(id)
				.orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + id));

		existing.setTitle(routeRequest.getTitle());
		existing.setType(routeRequest.getType());
		existing.setStartPoint(routeRequest.getStartPoint());
		existing.setEndPoint(routeRequest.getEndPoint());
		existing.setStatus(routeRequest.getStatus());

		Route updated = routeRepository.save(existing);

		// Optional: send notification on update
		RouteNotificationRequest notificationRequest = new RouteNotificationRequest(routeRequest.getEmail(),
				updated.getTitle());

		sendNotificationSafely(notificationRequest);

		return modelMapper.map(updated, RouteResponse.class);

	}

	@Override
	public List<RouteResponse> getAllRoutes() {
		return routeRepository.findAll().stream().map(r -> modelMapper.map(r, RouteResponse.class)).toList();
	}

	@Override
	public RouteResponse getRouteById(Long id) {
		Route route = routeRepository.findById(id)
				.orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + id));
		return modelMapper.map(route, RouteResponse.class);
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
		return routeRepository.findByType(type).stream().map(r -> modelMapper.map(r, RouteResponse.class)).toList();
	}

	@Override
	public int countActiveRoutes() {
		return routeRepository.countByStatus(RouteStatus.ACTIVE);
	}
	
	public void sendNotificationSafely(RouteNotificationRequest request) {
	    try {
	        notificationClient.sendRouteUpdateNotification(request);
	    } catch (FeignException.NotFound e) {
	        log.warn("Notification service returned 404, ignoring");
	    } catch (FeignException e) {
	        log.warn("Notification failed, ignored: {}", e.getMessage());
	    }
	}
}
