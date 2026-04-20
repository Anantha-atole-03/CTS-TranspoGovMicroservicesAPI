package com.cts.transport_gov.compliance_audit_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.transport_gov.compliance_audit_service.client.ProgramFeignClient;
import com.cts.transport_gov.compliance_audit_service.client.RouteFeignClient;
import com.cts.transport_gov.compliance_audit_service.client.TicketFeginCient;
import com.cts.transport_gov.compliance_audit_service.dto.ApiResponse;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceCreateRequest;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceResponse;
import com.cts.transport_gov.compliance_audit_service.dto.ComplianceUpdate;
import com.cts.transport_gov.compliance_audit_service.dto.ProgramResponse;
import com.cts.transport_gov.compliance_audit_service.dto.RouteResponse;
import com.cts.transport_gov.compliance_audit_service.dto.TicketResponse;
import com.cts.transport_gov.compliance_audit_service.enums.ComplianceResultStatus;
import com.cts.transport_gov.compliance_audit_service.enums.ComplianceType;
import com.cts.transport_gov.compliance_audit_service.exceptions.ComplianceNotFoundException;
import com.cts.transport_gov.compliance_audit_service.exceptions.ProgramNotFoundException;
import com.cts.transport_gov.compliance_audit_service.exceptions.RouteNotFoundException;
import com.cts.transport_gov.compliance_audit_service.exceptions.TicketNotFoundException;
import com.cts.transport_gov.compliance_audit_service.model.ComplianceRecord;
import com.cts.transport_gov.compliance_audit_service.repositories.ComplianceRecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceRecordService implements IComplianceRecordService {

	private final ComplianceRecordRepository repository;
	private final ModelMapper modelMapper;
	private final RouteFeignClient routeFeignClient;
	private final TicketFeginCient ticketFeignClient;
	private final ProgramFeignClient programFeignClient;

	@Override
	public List<ComplianceResponse> findAll() {
		log.info("Fetching all compliance records");
		return repository.findAll().stream().map(record -> modelMapper.map(record, ComplianceResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	public ComplianceResponse findById(Long id) {
		log.info("Fetching compliance record by id: {}", id);

		ComplianceRecord complianceRecord = repository.findById(id)
				.orElseThrow(() -> new ComplianceNotFoundException("Compliance Record not found"));

		// ✅ Feign call for validation / enrichment (no try-catch)
		if (complianceRecord.getType() == ComplianceType.ROUTE) {

			ResponseEntity<ApiResponse<RouteResponse>> route = routeFeignClient
					.getRouteById(complianceRecord.getEntityId());
			if (route == null || !route.hasBody()) {
				throw new RouteNotFoundException("Route Record not found");
			}

		} else if (complianceRecord.getType() == ComplianceType.TICKET) {

			ResponseEntity<TicketResponse> ticket = ticketFeignClient.getTicketById(complianceRecord.getEntityId());
			if (ticket == null || !ticket.hasBody()) {
				throw new TicketNotFoundException("Ticket Record not found");
			}
		} else if (complianceRecord.getType() == ComplianceType.PROGRAM) {
			ResponseEntity<ApiResponse<ProgramResponse>> program = programFeignClient
					.getProgramById(complianceRecord.getEntityId());
			if (program == null || !program.hasBody()) {
				throw new ProgramNotFoundException("Ticket Record not found");
			}
		}

		return modelMapper.map(complianceRecord, ComplianceResponse.class);
	}

	@Override
	public String create(ComplianceCreateRequest record) {
		log.info("Creating compliance record with type: {}", record.getType());

		// ✅ Feign validation BEFORE save (no try-catch)
		if (record.getType() == ComplianceType.ROUTE) {

			ResponseEntity<ApiResponse<RouteResponse>> route = routeFeignClient.getRouteById(record.getEntityId());
			if (route == null || !route.hasBody()) {
				throw new RouteNotFoundException("Route Record not found");
			}
		} else if (record.getType() == ComplianceType.TICKET) {
			ResponseEntity<TicketResponse> ticket = ticketFeignClient.getTicketById(record.getEntityId());
			if (ticket == null || !ticket.hasBody()) {
				throw new TicketNotFoundException("Ticket Record not found");
			}
		} else if (record.getType() == ComplianceType.PROGRAM) {
			ResponseEntity<ApiResponse<ProgramResponse>> program = programFeignClient
					.getProgramById(record.getEntityId());
			if (program == null || !program.hasBody()) {
				throw new ProgramNotFoundException("Ticket Record not found");
			}
		}

		ComplianceRecord saved = repository.save(modelMapper.map(record, ComplianceRecord.class));

		log.info("Compliance record created with id: {}", saved.getComplianceId());
		return "Record Saved successsfully";
	}

	@Override
	public ComplianceResponse update(Long id, ComplianceUpdate record) {

		ComplianceRecord existing = repository.findById(id)
				.orElseThrow(() -> new ComplianceNotFoundException("Compliance Record not found"));

		// ✅ Feign validation during update
		if (existing.getType() == ComplianceType.ROUTE) {

			ResponseEntity<ApiResponse<RouteResponse>> route = routeFeignClient.getRouteById(existing.getEntityId());
			if (route == null || !route.hasBody()) {
				throw new RouteNotFoundException("Route Record not found");
			}
		} else if (existing.getType() == ComplianceType.TICKET) {
			ResponseEntity<TicketResponse> ticket = ticketFeignClient.getTicketById(existing.getEntityId());
			if (ticket == null || !ticket.hasBody()) {
				throw new TicketNotFoundException("Ticket Record not found");
			}
		} else if (existing.getType() == ComplianceType.PROGRAM) {
			ResponseEntity<ApiResponse<ProgramResponse>> program = programFeignClient
					.getProgramById(existing.getEntityId());
			if (program == null || !program.hasBody()) {
				throw new ProgramNotFoundException("Ticket Record not found");
			}
		}

		existing.setType(existing.getType());
		existing.setResult(record.getResult());
		existing.setNotes(record.getNotes());
		existing.setEntityId(existing.getEntityId());

		return modelMapper.map(repository.save(existing), ComplianceResponse.class);
	}

	@Override
	public String delete(Long id) {
		log.info("Deleting compliance record id: {}", id);

		return repository.findById(id).map(record -> {
			repository.deleteById(record.getComplianceId());
			log.info("Compliance record deleted, id: {}", record.getComplianceId());
			return "Record deleted Successfully";
		}).orElseThrow(() -> new ComplianceNotFoundException("Compliance Record not found"));
	}

	@Override
	public List<ComplianceResponse> findByEntityId(Long entityId) {

		log.info("Fetching compliance records by entityId: {}", entityId);

		List<ComplianceRecord> records = repository.findByEntityId(entityId);

		if (records.isEmpty()) {
			log.warn("No compliance records found for entityId: {}", entityId);
			throw new ComplianceNotFoundException("No compliance records found for entityId: " + entityId);
		}

		return records.stream().map(record -> modelMapper.map(record, ComplianceResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	public Long getCount() {
		return repository.count();
	}

	@Override
	public int getComplianceAlerts() {
		return repository.countByResult(ComplianceResultStatus.FAIL);
	}
}