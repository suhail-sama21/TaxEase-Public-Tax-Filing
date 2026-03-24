package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.responsedto.AuditResponse;
import com.cognizant.taxease.dto.requestdto.CloseAuditRequest;

import java.util.List;

public interface AuditService {

    List<AuditResponse> getAllAudits();

    AuditResponse getAuditById(Long id);

    AuditResponse closeAudit(Long id, CloseAuditRequest request);
}
