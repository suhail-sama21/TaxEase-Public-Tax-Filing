package com.cognizant.taxease.service;

import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    Payment makePayment(Long filingId, PaymentMethod method, BigDecimal amount, StatusBasic status);
    List<Payment> getPaymentsByTaxpayer(Long taxpayerId);
    Payment retryPayment(Long oldPaymentId, PaymentMethod newMethod);
}