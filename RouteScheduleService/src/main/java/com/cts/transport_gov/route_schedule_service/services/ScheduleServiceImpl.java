package com.cts.transport_gov.route_schedule_service.services;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.route_schedule_service.dtos.ScheduleCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleResponse;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleUpdateRequest;
import com.cts.transport_gov.route_schedule_service.exceptions.RouteNotFoundException;
import com.cts.transport_gov.route_schedule_service.exceptions.ScheduleNotFoundException;
import com.cts.transport_gov.route_schedule_service.models.Route;
import com.cts.transport_gov.route_schedule_service.models.Schedule;
import com.cts.transport_gov.route_schedule_service.repository.RouteRepository;
import com.cts.transport_gov.route_schedule_service.repository.ScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

//Service implementation for managing Schedules.
// Handles business logic and interacts with the repository layer.

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements IScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

	private final ScheduleRepository scheduleRepository;
	private final RouteRepository routeRepository;
	private final ModelMapper modelMapper;

	@Override
	public ScheduleResponse addSchedule(Long routeId, ScheduleCreateRequest scheduleRequest) {
		logger.info("Adding schedule for routeId: {}", routeId);
		Route route = routeRepository.findById(routeId)
				.orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + routeId));

		// Validation: prevent past dates
		if (scheduleRequest.getDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Schedule date cannot be in the past.");
		}
		Schedule schedule = modelMapper.map(scheduleRequest, Schedule.class);
		schedule.setRoute(route);

		Schedule saved = scheduleRepository.save(schedule);
		logger.info("Schedule added successfully with id: {}", saved.getScheduleId());
		ScheduleResponse response = modelMapper.map(saved, ScheduleResponse.class);
		response.setRouteId(saved.getRoute().getRouteId());
		return response;
	}

	@Override
	public ScheduleResponse updateSchedule(Long id, ScheduleUpdateRequest scheduleRequest) {
		logger.info("Updating schedule with id: {}", id);
		Schedule existing = scheduleRepository.findById(id)
				.orElseThrow(() -> new ScheduleNotFoundException("Schedule not found with id: " + id));

		// Validation: prevent past dates
		if (scheduleRequest.getDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Schedule date cannot be in the past.");
		}

		existing.setDate(scheduleRequest.getDate());
		existing.setTime(scheduleRequest.getTime());
		existing.setStatus(scheduleRequest.getStatus());

		Schedule updated = scheduleRepository.save(existing);
		logger.info("Schedule updated successfully with id: {}", updated.getScheduleId());
		return modelMapper.map(updated, ScheduleResponse.class);
	}

	@Override
	public List<ScheduleResponse> getSchedulesByRoute(Long routeId) {
		logger.info("Fetching schedules for routeId: {}", routeId);
		return scheduleRepository.findByRoute_RouteId(routeId).stream()
				.map(schedule -> modelMapper.map(schedule, ScheduleResponse.class)).toList();
	}

	@Override
	public ScheduleResponse getScheduleById(Long id) {
		logger.info("Fetching schedule with id: {}", id);
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> new ScheduleNotFoundException("Schedule not found with id: " + id));
		return modelMapper.map(schedule, ScheduleResponse.class);
	}

	@Override
	public void deleteSchedule(Long id) {
		logger.info("Deleting schedule with id: {}", id);
		scheduleRepository.findById(id)
				.orElseThrow(() -> new ScheduleNotFoundException("Schedule not found with id: " + id));

		scheduleRepository.deleteById(id);
		logger.info("Schedule deleted successfully with id: {}", id);
		return;
	}
}
