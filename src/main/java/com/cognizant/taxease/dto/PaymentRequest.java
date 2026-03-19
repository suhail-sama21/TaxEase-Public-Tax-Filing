package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class PaymentRequest {
    private Long filingId;
    private PaymentMethod method;
    private BigDecimal amount;
    private StatusBasic status;
}