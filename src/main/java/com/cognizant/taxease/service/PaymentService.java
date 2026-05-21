package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.responsedto.PaymentMetricsResponse;
import com.cognizant.taxease.dto.responsedto.PaymentResponseDto;
import com.cognizant.taxease.dto.responsedto.RevenueDashboardResponse;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    PaymentResponseDto makePayment(Long filingId, PaymentMethod method, BigDecimal amount, StatusBasic status);
    List<PaymentResponseDto> getPaymentsByTaxpayer(Long taxpayerId);
    PaymentResponseDto retryPayment(Long oldPaymentId, PaymentMethod newMethod);

    // Admin dashboard methods
    PaymentMetricsResponse getPaymentMetrics();
    RevenueDashboardResponse getRevenueDashboard();
}