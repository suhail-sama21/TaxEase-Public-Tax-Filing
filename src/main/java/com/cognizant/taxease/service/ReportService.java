package com.cognizant.taxease.service;

import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.dto.responsedto.PaymentMetricsResponse;
import com.cognizant.taxease.dto.responsedto.AuditDashboardResponse;
import com.cognizant.taxease.dto.responsedto.RevenueDashboardResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    PaymentMetricsResponse getPaymentMetrics(PaymentMethod method);
    AuditDashboardResponse getAuditDashboard();
    RevenueDashboardResponse getRevenueDashboard(String period, String taxpayerType);
    List<Audit> getCompletedAudits();
    byte[] generateCustomReport(LocalDate startDate, LocalDate endDate, String reportType, List<String> metrics);
}