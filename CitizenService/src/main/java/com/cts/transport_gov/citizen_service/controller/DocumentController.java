package com.cts.transport_gov.citizen_service.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.transport_gov.citizen_service.dtos.DocumentResp;
import com.cts.transport_gov.citizen_service.dtos.DocumentUploadReq;
import com.cts.transport_gov.citizen_service.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles/documents")
public class DocumentController {

    private final DocumentService service;

    // Feature: View All for Officers/Auditors [Module 2.6]
    @GetMapping
    public ResponseEntity<List<DocumentResp>> getAll() {
        return ResponseEntity.ok(service.getAllDocuments());
    }

    // Feature: View specific profile documents
    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<List<DocumentResp>> getByCitizen(@PathVariable Long citizenId) {
        return ResponseEntity.ok(service.getByEntity(citizenId));
    }

    // Feature: Upload for Citizens [Module 2.2]
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestBody DocumentUploadReq req) {
        return new ResponseEntity<>(service.upload(req), HttpStatus.CREATED);
    }

    // Feature: Verification for Transport Officers [Module 4.2]
    @PatchMapping("/verify/{id}")
    public ResponseEntity<String> verify(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(service.verify(id, status));
    }
}