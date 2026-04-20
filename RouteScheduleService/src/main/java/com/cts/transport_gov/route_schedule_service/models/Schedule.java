package com.cts.transport_gov.route_schedule_service.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cts.transport_gov.route_schedule_service.enums.ScheduleStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "schedules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Schedule {
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "schedule_id", updatable = false, nullable = false)
  private Long scheduleId;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "route_id", nullable = false)
  private Route route;

  private LocalDate date;
  private LocalTime time;

  @Enumerated(EnumType.STRING)
  private ScheduleStatus status;
}