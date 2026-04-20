package com.cts.transport_gov.route_schedule_service.services;

import java.util.List;

import com.cts.transport_gov.route_schedule_service.dtos.ScheduleCreateRequest;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleResponse;
import com.cts.transport_gov.route_schedule_service.dtos.ScheduleUpdateRequest;

public interface IScheduleService {
	ScheduleResponse addSchedule(Long routeId, ScheduleCreateRequest schedule);

	ScheduleResponse updateSchedule(Long id, ScheduleUpdateRequest schedule);

	List<ScheduleResponse> getSchedulesByRoute(Long routeId);

	ScheduleResponse getScheduleById(Long id);

	void deleteSchedule(Long id);
}
