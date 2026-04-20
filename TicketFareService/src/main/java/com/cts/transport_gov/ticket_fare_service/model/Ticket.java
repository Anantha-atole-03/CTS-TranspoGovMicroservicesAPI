package com.cts.transport_gov.ticket_fare_service.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.cts.transport_gov.ticket_fare_service.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ticket_id", updatable = false, nullable = false)
	private Long ticketId;

	@Column(name = "citizen_id", nullable = false)
	private Long citizen;

	@Column(name = "route_id", nullable = false)
	private Long route;

	private LocalDateTime date;
	private Double fareAmount;

	@Enumerated(EnumType.STRING)
	private TicketStatus status;
	@CreationTimestamp
	private LocalDateTime createdAt;
}
