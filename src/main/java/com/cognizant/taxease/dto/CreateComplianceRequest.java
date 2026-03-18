package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.TaxFiling;

import com.cognizant.taxease.entity.entityEnum.ComplianceType;
import lombok.Data;

@lombok.Data
public class CreateComplianceRequest {

    private Long taxpayerId;
    private Long filingId;
    private Long paymentId;

    private ComplianceType type;
    private String result;
    private String notes;
}
