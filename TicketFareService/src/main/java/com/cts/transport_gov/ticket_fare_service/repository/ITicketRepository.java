package com.cts.transport_gov.ticket_fare_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.ticket_fare_service.model.Ticket;

@Repository
public interface ITicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findByCitizenOrderByDateDesc(Long citizenId);

}
