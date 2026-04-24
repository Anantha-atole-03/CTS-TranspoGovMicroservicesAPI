package com.cts.transport_gov.route_schedule_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.transport_gov.route_schedule_service.dtos.RouteCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.RouteResponse;
import com.cts.transport_gov.route_schedule_service.dtos.RouteUpdateRequest;
import com.cts.transport_gov.route_schedule_service.services.IRouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteController {

    private final IRouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(@RequestBody RouteCreateRequest route) {
    
        return ResponseEntity.status(HttpStatus.CREATED).body(routeService.addRoute(route));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> updateRoute(@PathVariable Long id,
                                                     @RequestBody RouteUpdateRequest route) {
        RouteResponse updated = routeService.updateRoute(id, route);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {
        List<RouteResponse> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable Long id) {
        RouteResponse route = routeService.getRouteById(id);
        return ResponseEntity.ok(route);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<RouteResponse>> getRoutesByType(@PathVariable String type) {
        List<RouteResponse> routes = routeService.getRoutesByType(type);
        return ResponseEntity.ok(routes);
    }
}
