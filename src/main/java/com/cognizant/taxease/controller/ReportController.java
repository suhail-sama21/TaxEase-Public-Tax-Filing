package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.PaymentMetricsResponse;
import com.cognizant.taxease.dto.AuditDashboardResponse;
import com.cognizant.taxease.dto.RevenueDashboardResponse;
import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // Story 1: Payment Success Metrics
    @GetMapping("/payments/metrics")
    public ResponseEntity<PaymentMetricsResponse> getPaymentMetrics(@RequestParam(required = false) PaymentMethod method) {
        return ResponseEntity.ok(reportService.getPaymentMetrics(method));
    }

    // Story 4: Audit Dashboard
    @GetMapping("/audits/dashboard")
    public ResponseEntity<AuditDashboardResponse> getAuditDashboard() {
        return ResponseEntity.ok(reportService.getAuditDashboard());
    }

    // Story 3: Read-Only Audit Reports
    @GetMapping("/audits/completed")
    public ResponseEntity<List<Audit>> getCompletedAudits() {
        return ResponseEntity.ok(reportService.getCompletedAudits());
    }

    // Story 5: Revenue Collection Dashboard
    @GetMapping("/revenue/dashboard")
    public ResponseEntity<RevenueDashboardResponse> getRevenueDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String taxpayerType) {
        return ResponseEntity.ok(reportService.getRevenueDashboard(period, taxpayerType));
    }

    // Story 2: Generate Downloadable Custom Report
    @GetMapping("/custom/download")
    public ResponseEntity<byte[]> downloadCustomReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String reportType,
            @RequestParam List<String> metrics) {

        byte[] reportData = reportService.generateCustomReport(startDate, endDate, reportType, metrics);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"taxease_custom_report.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(reportData);
    }
}