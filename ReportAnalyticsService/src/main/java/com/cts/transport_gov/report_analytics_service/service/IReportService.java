package com.cts.transport_gov.report_analytics_service.service;


import java.util.Map;

import com.cts.transport_gov.report_analytics_service.model.Report;



public interface IReportService {

	Map<String, Object> getOperationalDashboard();

	Report runCustomReport(String scope);

	Report getReportByJobId(Long jobId);
}

