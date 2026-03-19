package com.cognizant.taxease.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class TaxFilingResponseDTO {
    private Long id;
    private Long taxpayerId;
    private String period;
    private BigDecimal amountDeclared;
    private String status; // Kept as String for the frontend
    private Instant submittedDate;
}