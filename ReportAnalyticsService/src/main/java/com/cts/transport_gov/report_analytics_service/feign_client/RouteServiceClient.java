package com.cts.transport_gov.report_analytics_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "RouteScheduleService", fallback = RouteServiceFallback.class)

public interface RouteServiceClient {

	@GetMapping("/route/count/active")
	int countActiveRoutes();

}
