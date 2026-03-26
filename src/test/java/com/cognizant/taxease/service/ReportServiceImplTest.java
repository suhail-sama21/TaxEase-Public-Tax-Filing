package com.cognizant.taxease.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import com.cognizant.taxease.service.impl.ReportServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cognizant.taxease.dao.AuditRepository;
import com.cognizant.taxease.dao.ComplianceRecordRepository;
import com.cognizant.taxease.dao.PaymentRepository;
import com.cognizant.taxease.dao.RevenueRecordRepository;
import com.cognizant.taxease.dto.responsedto.AuditDashboardResponse;
import com.cognizant.taxease.dto.responsedto.PaymentMetricsResponse;
import com.cognizant.taxease.dto.responsedto.RevenueDashboardResponse;
import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private RevenueRecordRepository revenueRepository;

    @Mock
    private ComplianceRecordRepository complianceRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void testGetPaymentMetrics_WithoutMethodFilter() {
        // Arrange
        when(paymentRepository.countByStatus(StatusBasic.Completed)).thenReturn(100L);
        when(paymentRepository.countByStatus(StatusBasic.Failed)).thenReturn(5L);
        when(paymentRepository.count()).thenReturn(150L);

        // Act
        PaymentMetricsResponse response = reportService.getPaymentMetrics(null);

        // Assert
        assertNotNull(response);
        assertEquals(100L, response.getSuccessfulTransactions());
        assertEquals(5L, response.getFailedTransactions());
        assertEquals(150L, response.getTotalTransactions());
    }

    @Test
    void testGetPaymentMetrics_WithBankMethodFilter() {
        // Arrange
        when(paymentRepository.countByStatusAndMethod(StatusBasic.Completed, PaymentMethod.Bank)).thenReturn(50L);
        when(paymentRepository.countByStatusAndMethod(StatusBasic.Failed, PaymentMethod.Bank)).thenReturn(2L);
        when(paymentRepository.countByMethod(PaymentMethod.Bank)).thenReturn(60L);

        // Act
        PaymentMetricsResponse response = reportService.getPaymentMetrics(PaymentMethod.Bank);

        // Assert
        assertNotNull(response);
        assertEquals(50L, response.getSuccessfulTransactions());
        assertEquals(2L, response.getFailedTransactions());
        assertEquals(60L, response.getTotalTransactions());
    }

    @Test
    void testGetAuditDashboard() {
        // Arrange
        when(auditRepository.count()).thenReturn(50L);
        when(auditRepository.countByStatus(StatusBasic.Pending)).thenReturn(10L);
        when(auditRepository.countByStatus(StatusBasic.Completed)).thenReturn(40L);
        when(complianceRepository.countByResultIgnoreCase("Non-Compliant")).thenReturn(5L);

        // Act
        AuditDashboardResponse response = reportService.getAuditDashboard();

        // Assert
        assertNotNull(response);
        assertEquals(50L, response.getTotalAudits());
        assertEquals(10L, response.getOpenAudits());
        assertEquals(40L, response.getClosedAudits());
        assertEquals(5L, response.getNonComplianceFilings());
    }

    @Test
    void testGetRevenueDashboard() {
        // Arrange
        BigDecimal collected = new BigDecimal("50000.00");
        BigDecimal outstanding = new BigDecimal("10000.00");

        when(revenueRepository.sumCollectedRevenue()).thenReturn(collected);
        when(paymentRepository.sumOutstandingPayments()).thenReturn(outstanding); // Assuming this is where it's mapped based on earlier implementations

        // Act
        RevenueDashboardResponse response = reportService.getRevenueDashboard("Q1", "All");

        // Assert
        assertNotNull(response);
        assertEquals(collected, response.getRevenueCollected());
        assertEquals(outstanding, response.getOutstandingPayments());
    }

    @Test
    void testGetCompletedAudits() {
        // Arrange
        Audit audit1 = Audit.builder().id(1L).status(StatusBasic.Completed).build();
        Audit audit2 = Audit.builder().id(2L).status(StatusBasic.Completed).build();
        when(auditRepository.findByStatus(StatusBasic.Completed)).thenReturn(Arrays.asList(audit1, audit2));

        // Act
        List<Audit> response = reportService.getCompletedAudits();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(StatusBasic.Completed, response.get(0).getStatus());
    }

    @Test
    void testGenerateCustomReport_CSVFormat() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        String reportType = "Financial";
        List<String> metrics = Arrays.asList("Revenue", "Compliance");

        // Mocking empty lists for simplicity in the CSV generator test
        when(revenueRepository.findByDateBetween(any(Instant.class), any(Instant.class)))
                .thenReturn(Collections.emptyList());
        when(complianceRepository.findByDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        // Act
        byte[] csvBytes = reportService.generateCustomReport(startDate, endDate, reportType, metrics);
        String csvOutput = new String(csvBytes);

        // Assert
        assertNotNull(csvBytes);
        assertTrue(csvOutput.contains("TaxEase Dynamic Custom Report"));
        assertTrue(csvOutput.contains("Report Type:,Financial"));
        assertTrue(csvOutput.contains("2025-01-01"));
        assertTrue(csvOutput.contains("2025-12-31"));
        assertTrue(csvOutput.contains("--- REVENUE DATA ---"));
        assertTrue(csvOutput.contains("--- COMPLIANCE DATA ---"));
    }
}
