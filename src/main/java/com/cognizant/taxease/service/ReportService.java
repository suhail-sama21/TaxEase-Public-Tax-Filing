package com.cognizant.taxease.service;

import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.dto.responsedto.PaymentMetricsResponse;
import com.cognizant.taxease.dto.responsedto.AuditDashboardResponse;
import com.cognizant.taxease.dto.responsedto.RevenueDashboardResponse;
import com.cognizant.taxease.dto.responsedto.AuditResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    PaymentMetricsResponse getPaymentMetrics(PaymentMethod method);
    AuditDashboardResponse getAuditDashboard();
    RevenueDashboardResponse getRevenueDashboard(String period, String taxpayerType);

    // Updated Method for Pagination and DTO mapping 👇
    Page<AuditResponse> getCompletedAudits(Pageable pageable);

    byte[] generateCustomReport(LocalDate startDate, LocalDate endDate, String reportType, List<String> metrics);
}