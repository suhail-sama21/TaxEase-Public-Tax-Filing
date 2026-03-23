package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.PaymentRequest;
import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Endpoint for TAXFR-10: Make a payment
     * Accepts a JSON body via the PaymentRequest DTO
     */
    @PostMapping("/pay")
    public Payment makePayment(@RequestBody PaymentRequest request) {
        return paymentService.makePayment(
                request.getFilingId(),
                request.getMethod(),
                request.getAmount(),
                request.getStatus()
        );
    }

    /**
     * Endpoint for TAXFR-11: Get payment history
     */
    @GetMapping("/history/{taxpayerId}")
    public List<Payment> getPaymentHistory(@PathVariable Long taxpayerId) {
        return paymentService.getPaymentsByTaxpayer(taxpayerId);
    }

    /**
     * Endpoint for TAXFR-12: Retry a failed payment
     */
    @PostMapping("/retry/{oldPaymentId}")
    public Payment retryPayment(
            @PathVariable Long oldPaymentId,
            @RequestParam PaymentMethod newMethod) {
        return paymentService.retryPayment(oldPaymentId, newMethod);
    }
}