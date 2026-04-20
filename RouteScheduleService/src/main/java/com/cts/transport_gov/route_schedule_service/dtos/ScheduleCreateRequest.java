package com.cts.transport_gov.route_schedule_service.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cts.transport_gov.route_schedule_service.enums.ScheduleStatus;

import lombok.Data;

@Data
public class ScheduleCreateRequest {
	private Long routeId;
	private LocalDate date;
	private LocalTime time;
	private ScheduleStatus status;
}
