package com.cts.transport_gov.report_analytics_service.controller;


import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.report_analytics_service.dto.ReportRequest;
import com.cts.transport_gov.report_analytics_service.dto.ReportResponse;
import com.cts.transport_gov.report_analytics_service.enums.ReportScope;
import com.cts.transport_gov.report_analytics_service.enums.ReportStatus;
import com.cts.transport_gov.report_analytics_service.model.Report;
import com.cts.transport_gov.report_analytics_service.service.IReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/reports")
@Slf4j
@RequiredArgsConstructor
public class ReportController {

	
	private final IReportService reportService;

	/*
	 * GET /reports/operations
	 * 
	 * Fetches the operational dashboard metrics.
	 *
	 * @return ApiResponse containing dashboard details
	 * 
	 */

	@GetMapping("/operations")
	public ResponseEntity<Map<String, Object>> getDashboard() {
		log.info("Fetching operational dashboard");

		Map<String, Object> dashboard = reportService.getOperationalDashboard();

		return ResponseEntity.ok(dashboard);
	}

	/*
	 * POST /reports/custom/run
	 * 
	 * Starts execution of a custom report.
	 *
	 * @param request the report request payload
	 * 
	 * @return details of the initiated report job
	 * 
	 */

	@PostMapping("/custom/run")
	public ResponseEntity<ReportResponse> runReport(@RequestBody ReportRequest request) {
		log.info("Running report for scope={}", request.getScope());

		Report report = reportService.runCustomReport(request.getScope().name());

		ReportResponse response  = ReportResponse.builder().reportId(report.getReportId()).scope(request.getScope())
				.metrics(report.getMetrics()).status(ReportStatus.IN_PROGRESS).generatedDate(report.getGeneratedDate())
				.build();

		 return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/*
	 * reports/custom/jobs/id
	 * 
	 * Retrieves the status and result of a generated report.
	 *
	 * @param jobId the job identifier
	 * 
	 * @return the report details
	 * 
	 */

	@GetMapping("/custom/jobs/{jobId}")
	public ResponseEntity<ReportResponse> getJobStatus(@PathVariable Long jobId) {
		log.info("Fetching job status for jobId={}", jobId);

		Report report = reportService.getReportByJobId(jobId);

		ReportResponse response  = ReportResponse.builder().reportId(report.getReportId())
				.scope(ReportScope.valueOf(report.getScope())).metrics(report.getMetrics())
				.status(ReportStatus.COMPLETED).generatedDate(report.getGeneratedDate()).build();

		return ResponseEntity.ok(response);
	}

}
