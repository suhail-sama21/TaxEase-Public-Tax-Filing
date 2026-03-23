package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.AuditResponse;
import com.cognizant.taxease.dto.CloseAuditRequest;
import com.cognizant.taxease.service.AuditLogService;
import com.cognizant.taxease.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditResponse>> getAllAudits() {
        return ResponseEntity.ok(auditService.getAllAudits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditResponse> getAuditById(@PathVariable Long id) {
        return ResponseEntity.ok(auditService.getAuditById(id));
    }
   /// coo
    @PutMapping("/{id}/close")
    public ResponseEntity<AuditResponse> closeAudit(@PathVariable Long id,
                                                    @RequestBody CloseAuditRequest request) {
        return ResponseEntity.ok(auditService.closeAudit(id, request));
    }
}