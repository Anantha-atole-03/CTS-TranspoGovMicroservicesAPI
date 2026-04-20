package com.cts.transport_gov.report_analytics_service.dto;



import com.cts.transport_gov.report_analytics_service.enums.ReportScope;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {
    private ReportScope scope;
}