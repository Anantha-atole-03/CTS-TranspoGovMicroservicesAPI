package com.cts.transport_gov.citizen_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.citizen_service.dtos.CitizenProfileReq;
import com.cts.transport_gov.citizen_service.dtos.CitizenProfileResp;
import com.cts.transport_gov.citizen_service.dtos.ProfileUpdateReq;
import com.cts.transport_gov.citizen_service.enums.GenericStatus;
import com.cts.transport_gov.citizen_service.exception.ResourceNotFoundException;
import com.cts.transport_gov.citizen_service.model.AuditLog;
import com.cts.transport_gov.citizen_service.model.Citizen;
import com.cts.transport_gov.citizen_service.repository.AuditLogRepository;
import com.cts.transport_gov.citizen_service.repository.CitizenRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitizenServiceImpl implements CitizenService {

    private final CitizenRepository citizenRepo;
    private final AuditLogRepository auditRepo;

    @Override
    @Transactional
    public CitizenProfileResp register(CitizenProfileReq req) {
        log.info("Initiating citizen registration for: {}", req.getName());

        Citizen citizen = Citizen.builder()
                .name(req.getName())
                .dob(req.getDob())
                .gender(req.getGender())
                .address(req.getAddress())
                .contactInfo(req.getContactInfo())
                .status(GenericStatus.PENDING)
                .build();

        Citizen saved = citizenRepo.save(citizen);
        log.debug("Citizen saved with ID: {}", saved.getId());

        auditRepo.save(AuditLog.builder()
                .userId(saved.getId())
                .action("CITIZEN_REGISTRATION")
                .resource("Citizen_Profile")
                .timestamp(LocalDateTime.now())
                .build());
        
        log.info("Successfully registered citizen ID: {} and created audit entry.", saved.getId());
        return mapToResp(saved);
    }

    @Override
    @Transactional
    public CitizenProfileResp updateProfile(Long id, ProfileUpdateReq req) {
        log.info("Request to update profile for Citizen ID: {}", id);

        Citizen citizen = citizenRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Update failed: Citizen with ID {} not found", id);
                    return new ResourceNotFoundException("Citizen not found");
                });

        if (req.getAddress() != null) citizen.setAddress(req.getAddress());
        if (req.getContactInfo() != null) citizen.setContactInfo(req.getContactInfo());
        if (req.getStatus() != null) {
            log.debug("Updating status to: {}", req.getStatus());
            citizen.setStatus(GenericStatus.valueOf(req.getStatus().toUpperCase()));
        }

        Citizen updated = citizenRepo.save(citizen);

        auditRepo.save(AuditLog.builder()
                .userId(updated.getId())
                .action("PROFILE_UPDATE")
                .resource("Citizen_Profile")
                .timestamp(LocalDateTime.now())
                .build());

        log.info("Profile update completed for Citizen ID: {}", id);
        return mapToResp(updated);
    }

    @Override @Transactional(readOnly = true)
    public List<CitizenProfileResp> getAll() {
        log.debug("Fetching all citizen profiles");
        return citizenRepo.findAll().stream().map(this::mapToResp).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public CitizenProfileResp getById(Long id) {
        log.debug("Fetching profile for ID: {}", id);
        return mapToResp(citizenRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Profile fetch failed: ID {} not found", id);
                    return new ResourceNotFoundException("Not found");
                }));
    }

    /**
     * Internal mapper to convert Entity to DTO.
     * Logs mapping details for debugging data consistency.
     */
    private CitizenProfileResp mapToResp(Citizen c) {
        CitizenProfileResp resp = new CitizenProfileResp();
        resp.setId(c.getId());
        resp.setName(c.getName());
        resp.setStatus(c.getStatus() != null ? c.getStatus().name() : "PENDING");
        resp.setAddress(c.getAddress());
        resp.setContactInfo(c.getContactInfo());
        
        resp.setCreatedDate(c.getCreatedDate());
        resp.setLastModifiedDate(c.getLastModifiedDate());
        
        return resp;
    }
    
    /**
     * Service Command: Clears audit logs for a specific user.
     * (Example of a management command using the logger)
     */
    public void executeCleanupCommand(Long userId) {
        log.warn("COMMAND: Executing cleanup for User ID: {}", userId);
        // Implementation logic here
    }
}