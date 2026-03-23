package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dao.AuditRepository;
import com.cognizant.taxease.dto.AuditResponse;
import com.cognizant.taxease.dto.CloseAuditRequest;
import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    public List<AuditResponse> getAllAudits() {
        return auditRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AuditResponse getAuditById(Long id) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Audit not found with ID: " + id));
        return mapToResponse(audit);
    }

    @Override
    public AuditResponse closeAudit(Long id, CloseAuditRequest request) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Audit not found with ID: " + id));

        // Update findings if provided in the request
        if (request.getFindings() != null && !request.getFindings().isBlank()) {
            audit.setFindings(request.getFindings());
        }

        // Set status to Inactive/Closed as per business logic
        audit.setStatus(StatusBasic.Inactive);

        Audit savedAudit = auditRepository.save(audit);
        return mapToResponse(savedAudit);
    }

    /**
     * Converts Audit Entity to AuditResponse DTO using Builder pattern.
     * This avoids "Incompatible Types" errors caused by field ordering.
     */
    private AuditResponse mapToResponse(Audit audit) {
        return AuditResponse.builder()
                .id(audit.getId())
                .officerId(audit.getOfficer() != null ? audit.getOfficer().getId() : null)
                .scope(audit.getScope())
                .findings(audit.getFindings())
                .status(audit.getStatus())
                .createdAt(audit.getCreatedAt())
                .build();
    }
}