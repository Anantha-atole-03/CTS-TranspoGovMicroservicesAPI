package com.cts.transport_gov.route_schedule_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteNotificationRequest {
    private String email;
    private String route;
}
