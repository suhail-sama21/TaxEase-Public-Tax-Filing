package com.cognizant.taxease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMetricsResponse {
    private long successfulTransactions;
    private long failedTransactions;
    private long totalTransactions;
}