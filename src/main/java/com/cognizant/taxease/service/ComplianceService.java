package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.CreateComplianceRequest;
import com.cognizant.taxease.entity.ComplianceRecord;

import java.util.List;

/**
 * Service contract for managing compliance records.
 */
public interface ComplianceService {

    /**
     * Retrieve all compliance records.
     *
     * @return list of all {@link ComplianceRecord}
     */
    List<ComplianceRecord> getAllCompliance();

    /**
     * Create a new compliance record based on the request.
     *
     * @param request payload containing taxpayer, type (Filing/Payment), and related data
     * @return the persisted {@link ComplianceRecord}
     * @throws java.util.NoSuchElementException if taxpayer/filing/payment referenced in request is not found
     * @throws IllegalArgumentException if the request is invalid or inconsistent for the given type
     */
    ComplianceRecord createCompliance(CreateComplianceRequest request);
}