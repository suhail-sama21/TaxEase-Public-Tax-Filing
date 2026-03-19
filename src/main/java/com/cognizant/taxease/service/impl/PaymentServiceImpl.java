package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dao.PaymentRepository;
import com.cognizant.taxease.dao.RevenueRecordRepository;
import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.RevenueRecord;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TaxFilingRepository taxFilingRepository;

    @Autowired
    private RevenueRecordRepository revenueRecordRepository;

    @Override
    @Transactional
    public Payment makePayment(Long filingId, PaymentMethod method, BigDecimal amount, StatusBasic status) {
        // TAXFR-10: Make online tax payment using filing ID
        TaxFiling filing = taxFilingRepository.findById(filingId)
                .orElseThrow(() -> new RuntimeException("Filing not found"));

        Payment payment = Payment.builder()
                .filing(filing)
                .method(method)
                .amount(amount)
                .status(status)
                .build();

        payment = paymentRepository.save(payment);

        // AUDI-4: Only create RevenueRecord if payment is Completed (Success)
        if (status == StatusBasic.Completed) {
            RevenueRecord revenueRecord = RevenueRecord.builder()
                    .taxpayer(filing.getTaxpayer())
                    .payment(payment)
                    .amount(amount)
                    .status(StatusBasic.Completed)
                    .build();
            revenueRecordRepository.save(revenueRecord);
        }

        return payment;
    }

    @Override
    public List<Payment> getPaymentsByTaxpayer(Long taxpayerId) {
        // TAXFR-11: View previous payments for logged-in user
        return paymentRepository.findByFiling_Taxpayer_Id(taxpayerId);
    }

    @Override
    @Transactional
    public Payment retryPayment(Long oldPaymentId, PaymentMethod newMethod) {
        // TAXFR-12: Retry a failed payment
        Payment oldPayment = paymentRepository.findById(oldPaymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (oldPayment.getStatus() != StatusBasic.Failed) {
            throw new RuntimeException("Only failed payments can be retried");
        }

        // Creates a new record for the retry; the old one remains Failed
        return makePayment(
                oldPayment.getFiling().getId(),
                newMethod,
                oldPayment.getAmount(),
                StatusBasic.Completed // Assuming success for the retry
        );
    }
}