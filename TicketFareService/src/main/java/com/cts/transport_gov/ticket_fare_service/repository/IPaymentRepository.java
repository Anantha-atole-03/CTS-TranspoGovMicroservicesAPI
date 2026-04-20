package com.cts.transport_gov.ticket_fare_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.ticket_fare_service.model.Payment;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {
}