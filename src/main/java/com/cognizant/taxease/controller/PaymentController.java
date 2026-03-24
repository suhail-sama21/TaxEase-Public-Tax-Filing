package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.PaymentRequest;
import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Endpoint for TAXFR-10: Make a payment
     * Accepts a JSON body via the PaymentRequest DTO
     */
    @PostMapping("/pay")
    public Payment makePayment(@RequestBody PaymentRequest request) {
        log.info("START: Initiating payment for Filing ID: {} | Amount: {}", request.getFilingId(), request.getAmount());
        Payment response = paymentService.makePayment(request.getFilingId(), request.getMethod(), request.getAmount(), request.getStatus());
        log.info("END: Payment processed | Transaction ID: {} | Status: {}", response.getId(), response.getStatus());
        return response;
    }

    /**
     * Endpoint for TAXFR-11: Get payment history
     */
    @GetMapping("/history/{taxpayerId}")
    public List<Payment> getPaymentHistory(@PathVariable Long taxpayerId) {
        log.info("START: Fetching payment history for taxpayer {}", taxpayerId);
        List<Payment> response = paymentService.getPaymentsByTaxpayer(taxpayerId);
        log.info("END: Retrieved {} payment records", response.size());
        return response;
    }

    /**
     * Endpoint for TAXFR-12: Retry a failed payment
     */
    @PostMapping("/retry/{oldPaymentId}")
    public Payment retryPayment(
            @PathVariable Long oldPaymentId,
            @RequestParam PaymentMethod newMethod) {
        log.info("START: Retrying payment for old ID: {} | New Method: {}", oldPaymentId, newMethod);
        Payment response = paymentService.retryPayment(oldPaymentId, newMethod);
        log.info("END: Retry payment processed | New Transaction ID: {}", response.getId());
        return response;
    }
}