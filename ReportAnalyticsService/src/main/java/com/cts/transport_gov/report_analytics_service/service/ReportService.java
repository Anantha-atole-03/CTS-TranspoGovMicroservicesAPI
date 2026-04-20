package com.cts.transport_gov.report_analytics_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cts.transport_gov.report_analytics_service.exception.ResourceNotFoundException;
import com.cts.transport_gov.report_analytics_service.exception.ServiceUnavailableException;
import com.cts.transport_gov.report_analytics_service.feign_client.ComplianceServiceClient;
import com.cts.transport_gov.report_analytics_service.feign_client.ProgramServiceClient;
import com.cts.transport_gov.report_analytics_service.feign_client.RouteServiceClient;
import com.cts.transport_gov.report_analytics_service.feign_client.TicketServiceClient;
import com.cts.transport_gov.report_analytics_service.model.Report;
import com.cts.transport_gov.report_analytics_service.repository.IReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReportService implements IReportService {

	private final IReportRepository reportRepo;
	// Feign Clients
	private final RouteServiceClient routeServiceClient;
	private final TicketServiceClient ticketServiceClient;
	private final ProgramServiceClient programServiceClient;
	private final ComplianceServiceClient complianceServiceClient;

	/*
	 * Description: Generates the operational dashboard by aggregating key metrics
	 * from routes, tickets, compliance records, and transport programs. It collects
	 * data from multiple service layers and returns a consolidated Map containing
	 * essential operational insights.
	 */

	@Override
	public Map<String, Object> getOperationalDashboard() {
		log.info("Generating operational dashboard");

		Map<String, Object> response = new HashMap<>();
		try {
			response.put("activeRoutes", routeServiceClient.countActiveRoutes());
			response.put("totalTickets", ticketServiceClient.countTickets());
			response.put("complianceAlerts", complianceServiceClient.getComplianceAlerts());
			response.put("programEfficiency", programServiceClient.calculateEfficiency());
		} catch (FeignException.NotFound ex) {
			throw new ResourceNotFoundException("One of the dashboard APIs was not found");
		} catch (FeignException ex) {
			throw new ServiceUnavailableException("One or more dependent services are unavailable");
		}

		return response;
	}

	/*
	 * Description: Executes a custom report based on the provided scope. It gathers
	 * operational metrics across routes, tickets, compliance alerts, and program
	 * efficiency, converts them into JSON format, and stores the report in the
	 * database before returning the saved Report entity.
	 */

	@Override
	public Report runCustomReport(String scope) {
		log.info("Running custom report for scope={}", scope);

		Map<String, Object> metrics = new HashMap<>();

		metrics.put("activeRoutes", routeServiceClient.countActiveRoutes());
		metrics.put("totalTickets", ticketServiceClient.countTickets());
		metrics.put("complianceAlerts", complianceServiceClient.getComplianceAlerts());
		metrics.put("programEfficiency", programServiceClient.calculateEfficiency());

		String jsonMetrics;
		try {
			jsonMetrics = new ObjectMapper().writeValueAsString(metrics);
		} catch (Exception e) {
			throw new RuntimeException("JSON conversion error");
		}

		Report report = Report.builder().scope(scope).metrics(jsonMetrics).build();
		return reportRepo.save(report);
	}

	/*
	 * Description: Retrieves a stored report using its jobId. It fetches the report
	 * record from the database and returns the Report entity. If the report does
	 * not exist, it throws an exception indicating that the report was not found.
	 */

	@Override
	public Report getReportByJobId(Long jobId) {
		log.info("Fetching report jobId={}", jobId);
		return reportRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Report not found"));

	}

}
