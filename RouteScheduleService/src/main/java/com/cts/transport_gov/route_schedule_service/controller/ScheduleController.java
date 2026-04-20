package com.cts.transport_gov.route_schedule_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.transport_gov.route_schedule_service.dtos.ScheduleCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleResponse;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleUpdateRequest;
import com.cts.transport_gov.route_schedule_service.services.IScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final IScheduleService scheduleService;

    @PostMapping("/{routeId}")
    public ResponseEntity<ScheduleResponse> addSchedule(@PathVariable Long routeId,
                                                        @RequestBody ScheduleCreateRequest schedule) {
        ScheduleResponse created = scheduleService.addSchedule(routeId, schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id,
                                                           @RequestBody ScheduleUpdateRequest schedule) {
        ScheduleResponse updated = scheduleService.updateSchedule(id, schedule);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByRoute(@PathVariable Long routeId) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByRoute(routeId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long id) {
        ScheduleResponse schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
