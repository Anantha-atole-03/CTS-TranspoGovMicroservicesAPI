package com.cts.transport_gov.route_schedule_service.models;

import java.util.List;

import com.cts.transport_gov.route_schedule_service.enums.RouteStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // that automatically generates a builder pattern for your class
public class Route {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // identity //sequence //table
	@Column(name = "route_id", updatable = false, nullable = false)
	private Long routeId;

	private String title;
	private String type;

	private String startPoint;
	private String endPoint;

	@Enumerated(EnumType.STRING) // store the enum name as text in db
	private RouteStatus status;
	// One route can have many schedules
	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Schedule> schedules;
}