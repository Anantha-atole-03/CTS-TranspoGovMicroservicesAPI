package com.cts.transport_gov.route_schedule_service.feign_client;

import com.cts.transport_gov.route_schedule_service.dtos.RouteNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATIONSERVICE", dismiss404 = true)
public interface NotificationClient {

    @PostMapping("/notification/route")  
    void sendRouteUpdateNotification(@RequestBody RouteNotificationRequest request);
}
