package com.cts.transport_gov.report_analytics_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ReportAnalyticsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportAnalyticsServiceApplication.class, args);
	}

}
