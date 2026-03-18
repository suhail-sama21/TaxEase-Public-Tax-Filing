package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dto.CreateComplianceRequest;
import com.cognizant.taxease.entity.ComplianceRecord;
import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.entityEnum.ComplianceType;
import com.cognizant.taxease.dao.ComplianceRecordRepository;
import com.cognizant.taxease.dao.PaymentRepository;
import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.dao.TaxpayerRepository;
import com.cognizant.taxease.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ComplianceServiceImpl implements ComplianceService {

    private final ComplianceRecordRepository complianceRecordRepository;
    private final TaxpayerRepository taxpayerRepository;
    private final TaxFilingRepository taxFilingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<ComplianceRecord> getAllCompliance() {
        return complianceRecordRepository.findAll();
    }

    @Override
    public ComplianceRecord createCompliance(CreateComplianceRequest request) {
        ComplianceRecord record = new ComplianceRecord();

        Taxpayer taxpayer = taxpayerRepository.findById(request.getTaxpayerId())
                .orElseThrow(() -> new NoSuchElementException("Taxpayer not found"));
        record.setTaxpayer(taxpayer);

        if (request.getType() == ComplianceType.Filing) {
            if (request.getFilingId() == null) {
                throw new IllegalArgumentException("filingId is required when type is Filing");
            }
            TaxFiling filing = taxFilingRepository.findById(request.getFilingId())
                    .orElseThrow(() -> new NoSuchElementException("Filing not found"));
            record.setFiling(filing);
            record.setPayment(null);
        } else if (request.getType() == ComplianceType.Payment) {
            if (request.getPaymentId() == null) {
                throw new IllegalArgumentException("paymentId is required when type is Payment");
            }
            Payment payment = paymentRepository.findById(request.getPaymentId())
                    .orElseThrow(() -> new NoSuchElementException("Payment not found"));
            record.setPayment(payment);
            record.setFiling(null);
        } else {
            throw new IllegalArgumentException("Unsupported compliance type: " + request.getType());
        }

        record.setType(request.getType());
        record.setResult(request.getResult());
        record.setNotes(request.getNotes());
        return complianceRecordRepository.save(record);
    }
}