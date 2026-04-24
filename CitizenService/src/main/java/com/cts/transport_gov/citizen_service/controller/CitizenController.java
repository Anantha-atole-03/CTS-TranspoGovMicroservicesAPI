package com.cts.transport_gov.citizen_service.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.cts.transport_gov.citizen_service.dtos.CitizenProfileReq;
import com.cts.transport_gov.citizen_service.dtos.CitizenProfileResp;
import com.cts.transport_gov.citizen_service.dtos.ProfileUpdateReq;
import com.cts.transport_gov.citizen_service.service.CitizenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Added for logging

@Slf4j // Injects 'log' field
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles/citizens")
public class CitizenController {
    
    private final CitizenService service;

    // Feature: Registers citizens [Module 4.2]
    @PostMapping("/register")
    public CitizenProfileResp register(@RequestBody CitizenProfileReq req) {
        log.info("REST request to register citizen: {}", req);
        return service.register(req);
    }

    // Feature: Maintains profiles
    @GetMapping
    public List<CitizenProfileResp> getAll() {
        log.info("REST request to get all citizen profiles");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CitizenProfileResp getById(@PathVariable Long id) {
        log.info("REST request to get citizen by ID: {}", id);
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public CitizenProfileResp update(@PathVariable Long id, @RequestBody ProfileUpdateReq req) {
        log.info("REST request to update citizen ID: {} with data: {}", id, req);
        return service.updateProfile(id, req);
    }
}