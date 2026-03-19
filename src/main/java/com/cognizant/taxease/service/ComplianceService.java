package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.CreateComplianceRequest;
import com.cognizant.taxease.dto.ComplianceResponse;
import com.cognizant.taxease.dto.UpdateComplianceRequest;
import com.cognizant.taxease.entity.ComplianceRecord;

import java.util.List;

public interface ComplianceService {

    List<ComplianceResponse> getAllCompliance();

    ComplianceResponse createCompliance(CreateComplianceRequest request);

    ComplianceResponse getComplianceById(Long id);

    ComplianceResponse updateCompliance(Long id, UpdateComplianceRequest request);

    List<ComplianceResponse>getComplianceByTaxpayerId(Long taxpayerId);

    List<ComplianceResponse> getByResult(String result);
}
