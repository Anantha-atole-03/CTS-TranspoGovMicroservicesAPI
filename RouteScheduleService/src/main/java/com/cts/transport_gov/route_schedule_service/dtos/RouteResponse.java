package com.cts.transport_gov.route_schedule_service.dtos;


import com.cts.transport_gov.route_schedule_service.enums.RouteStatus;

import lombok.Data;

@Data
public class RouteResponse {
  private Long routeId;
  private String title;
  private String type;
  private String startPoint;
  private String endPoint;
  private RouteStatus status;
}
