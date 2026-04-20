package com.cts.transport_gov.report_analytics_service.dto;


import java.time.LocalDateTime;

import com.cts.transport_gov.report_analytics_service.enums.ReportScope;
import com.cts.transport_gov.report_analytics_service.enums.ReportStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportResponse {
    private Long reportId;
    private ReportScope scope;
    private String metrics;
    private ReportStatus status;
    private LocalDateTime generatedDate;
}