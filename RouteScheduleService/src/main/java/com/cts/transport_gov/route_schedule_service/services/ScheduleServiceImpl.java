package com.cts.transport_gov.route_schedule_service.services;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.route_schedule_service.dtos.ScheduleCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleResponse;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleUpdateRequest;
import com.cts.transport_gov.route_schedule_service.exceptions.RouteNotFoundException;
import com.cts.transport_gov.route_schedule_service.exceptions.ScheduleNotFoundException;
import com.cts.transport_gov.route_schedule_service.models.Route;
import com.cts.transport_gov.route_schedule_service.models.Schedule;
import com.cts.transport_gov.route_schedule_service.repository.RouteRepository;
import com.cts.transport_gov.route_schedule_service.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements IScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final RouteRepository routeRepository;
	private final ModelMapper modelMapper;

	@Override
	public ScheduleResponse addSchedule(Long routeId, ScheduleCreateRequest request) {
		Route route = routeRepository.findById(routeId)
				.orElseThrow(() -> new RouteNotFoundException("Route ID " + routeId + " not found"));

		if (request.getDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Schedule date cannot be in the past.");
		}

		Schedule schedule = modelMapper.map(request, Schedule.class);
		schedule.setRoute(route); // Manually link the route entity

		Schedule saved = scheduleRepository.save(schedule);
		return modelMapper.map(saved, ScheduleResponse.class);
	}

	@Override
	public ScheduleResponse updateSchedule(Long id, ScheduleUpdateRequest request) {
		Schedule existing = scheduleRepository.findById(id)
				.orElseThrow(() -> new ScheduleNotFoundException("Schedule ID " + id + " not found"));

		if (request.getDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Updated date cannot be in the past.");
		}

		existing.setDate(request.getDate());
		existing.setTime(request.getTime());
		existing.setStatus(request.getStatus());

		return modelMapper.map(scheduleRepository.save(existing), ScheduleResponse.class);
	}

	@Override
	public List<ScheduleResponse> getSchedulesByRoute(Long routeId) {
		return scheduleRepository.findByRoute_RouteId(routeId).stream()
				.map(s -> modelMapper.map(s, ScheduleResponse.class)).toList();
	}

	@Override
	public ScheduleResponse getScheduleById(Long id) {
		Schedule s = scheduleRepository.findById(id)
				.orElseThrow(() -> new ScheduleNotFoundException("Schedule ID " + id + " not found"));
		return modelMapper.map(s, ScheduleResponse.class);
	}

	@Override
	public void deleteSchedule(Long id) {
		if (!scheduleRepository.existsById(id)) {
			throw new ScheduleNotFoundException("ID " + id + " not found");
		}
		scheduleRepository.deleteById(id);
	}
}