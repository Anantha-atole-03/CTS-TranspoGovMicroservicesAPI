package com.cts.transport_gov.ticket_fare_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TicketFareServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketFareServiceApplication.class, args);
	}

}
