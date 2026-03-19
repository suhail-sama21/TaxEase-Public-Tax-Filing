package com.cognizant.taxease.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TaxFilingRequestDTO {
    private Long taxpayerId;
    private String period;
    private BigDecimal amountDeclared;
}