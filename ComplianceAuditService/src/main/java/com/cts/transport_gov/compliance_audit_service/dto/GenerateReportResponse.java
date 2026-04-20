package com.cts.transport_gov.compliance_audit_service.dto;

import java.time.LocalDateTime;

public class GenerateReportResponse {
    private Long auditId;
    private String reportUrl;
    private LocalDateTime generatedAt;

    public Long getAuditId() { return auditId; }
    public void setAuditId(Long auditId) { this.auditId = auditId; }
    public String getReportUrl() { return reportUrl; }
    public void setReportUrl(String reportUrl) { this.reportUrl = reportUrl; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}