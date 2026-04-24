package com.cts.transport_gov.route_schedule_service;

import com.cts.transport_gov.route_schedule_service.dtos.ScheduleResponse;
import com.cts.transport_gov.route_schedule_service.models.Schedule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class RouteScheduleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteScheduleServiceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        
        // 1. Set configuration for strict matching to avoid accidental wrong mappings
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(Conditions.isNotNull());

        // 2. CRITICAL FIX for the "Null" Issue:
        // This tells the mapper: "When going from Schedule to ScheduleResponse, 
        // look inside the Route object and take the RouteId."
        mapper.typeMap(Schedule.class, ScheduleResponse.class).addMappings(m -> {
            m.map(src -> src.getRoute().getRouteId(), ScheduleResponse::setRouteId);
        });

        return mapper;
    }
}