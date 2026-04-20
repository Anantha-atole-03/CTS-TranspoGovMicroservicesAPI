package com.cts.transport_gov.route_schedule_service.services;

import com.cts.transport_gov.route_schedule_service.dtos.*;
import com.cts.transport_gov.route_schedule_service.enums.RouteStatus;
import com.cts.transport_gov.route_schedule_service.exceptions.RouteNotFoundException;
import com.cts.transport_gov.route_schedule_service.models.Route;
import com.cts.transport_gov.route_schedule_service.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements IRouteService {

    private final ModelMapper modelMapper;
    private final RouteRepository routeRepository;

    @Override
    public RouteResponse addRoute(RouteCreateRequest routeRequest) {
        Route route = modelMapper.map(routeRequest, Route.class);
        Route saved = routeRepository.save(route);
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

        return modelMapper.map(routeRepository.save(existing), RouteResponse.class);
    }

    @Override
    public List<RouteResponse> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(r -> modelMapper.map(r, RouteResponse.class)).toList();
    }

    @Override
    public RouteResponse getRouteById(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + id));
        return modelMapper.map(route, RouteResponse.class);
    }

    @Override
    public void deleteRoute(Long id) {
        if (!routeRepository.existsById(id)) throw new RouteNotFoundException("Route ID " + id + " not found");
        routeRepository.deleteById(id);
    }

    @Override
    public List<RouteResponse> getRoutesByType(String type) {
        return routeRepository.findByType(type).stream()
                .map(r -> modelMapper.map(r, RouteResponse.class)).toList();
    }

    @Override
    public int countActiveRoutes() {
        return routeRepository.countByStatus(RouteStatus.ACTIVE);
    }
}