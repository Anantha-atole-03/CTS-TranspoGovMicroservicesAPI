package com.cts.transport_gov.report_analytics_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cts.transport_gov.report_analytics_service.dto.FullAnalyticsResponse;
import com.cts.transport_gov.report_analytics_service.dto.ProgramUtilizationDTO;
import com.cts.transport_gov.report_analytics_service.exception.ResourceNotFoundException;
import com.cts.transport_gov.report_analytics_service.exception.ServiceUnavailableException;
import com.cts.transport_gov.report_analytics_service.feign_client.ComplianceServiceClient;
import com.cts.transport_gov.report_analytics_service.feign_client.ProgramServiceClient;
import com.cts.transport_gov.report_analytics_service.feign_client.ResourceServiceClient;
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
	private final ResourceServiceClient resourceServiceClient;

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
		} catch (FeignException ex) {
			response.put("activeRoutes", 0); // fallback
			log.error("Route service failed", ex);
		}

		try {
			response.put("totalTickets", ticketServiceClient.countTickets());
		} catch (FeignException ex) {
			response.put("totalTickets", 0L);
			log.error("Ticket service failed", ex);
		}

		try {
			response.put("complianceAlerts", complianceServiceClient.getComplianceAlerts());
		} catch (FeignException ex) {
			response.put("complianceAlerts", 0);
			log.error("Compliance service failed", ex);
		}

		try {
			response.put("programEfficiency", programServiceClient.calculateEfficiency());
		} catch (FeignException ex) {
			response.put("programEfficiency", 0.0);
			log.error("Program service failed", ex);
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

		try {
			metrics.put("activeRoutes", routeServiceClient.countActiveRoutes());
			log.info("route-work");
			metrics.put("totalTickets", ticketServiceClient.countTickets());
			log.info("tiket-work");
			metrics.put("complianceAlerts", complianceServiceClient.getComplianceAlerts());
			log.info("compliance-work");
			metrics.put("programEfficiency", programServiceClient.calculateEfficiency());
			log.info("program-work");
		} catch (FeignException.NotFound ex) {
			throw new ResourceNotFoundException("One of the dependent report APIs was not found");
		} catch (FeignException ex) {
			throw new ServiceUnavailableException("One or more dependent services are unavailable");
		}

		try {
			String jsonMetrics = new ObjectMapper().writeValueAsString(metrics);
			Report report = Report.builder().scope(scope).metrics(jsonMetrics).build();
			return reportRepo.save(report);
		} catch (Exception e) {
			throw new RuntimeException("JSON conversion error");
		}
	}

	/*
	 * Description: Retrieves a stored report using its jobId. It fetches the report
	 * record from the database and returns the Report entity. If the report does
	 * not exist, it throws an exception indicating that the report was not found.
	 */

	@Override
	public Report getReportByJobId(Long jobId) {
		log.info("Fetching report jobId={}", jobId);
		return reportRepo.findById(jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + jobId));
	}

	@Override
	public FullAnalyticsResponse getFullAnalytics(Long programId) {
		{

			log.info("Fetching full analytics for programId={}", programId);

			int activeRoutes = 0;
			long totalTickets = 0;
			int complianceAlerts = 0;

			ProgramUtilizationDTO utilization;

			try {
				activeRoutes = routeServiceClient.countActiveRoutes();
				totalTickets = ticketServiceClient.countTickets();
				complianceAlerts = complianceServiceClient.getComplianceAlerts();

				utilization = resourceServiceClient.getProgramUtilization(programId);

// ✅ FIX: Add debug log (VERY IMPORTANT)
				log.info("Utilization received from Resource Service: {}", utilization);

				// ✅ FIX: Handle null response
				if (utilization == null) {
					log.error("Utilization is NULL for programId={}", programId);
					throw new ServiceUnavailableException("Resource service returned NULL data");
				}

			} catch (FeignException ex) {
				log.error("Dependency failure", ex);
				throw new ServiceUnavailableException("Dependent service failed");
			}

			// ✅ DERIVED METRICS (REAL ANALYTICS)

			double ticketPerRoute = activeRoutes == 0 ? 0 : (double) totalTickets / activeRoutes;

			double resourceEfficiency = utilization.getTotalResourcesAllocated() == 0 ? 0
					: (double) utilization.getTotalResourcesUsed() / utilization.getTotalResourcesAllocated() * 100;

			double budgetEfficiency = utilization.getBudgetUtilizationPercentage();
			return FullAnalyticsResponse.builder().activeRoutes(activeRoutes).totalTickets(totalTickets)
					.complianceAlerts(complianceAlerts)

					// ✅ ✅ ADD THIS LINE (IMPORTANT FIX)
					.programUtilization(utilization)

					.ticketPerRoute(ticketPerRoute).resourceEfficiency(resourceEfficiency)
					.budgetEfficiency(budgetEfficiency).build();
		}

	}
}
