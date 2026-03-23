package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.AuditLogResponse;
import com.cognizant.taxease.entity.AuditLog;
import com.cognizant.taxease.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;


    @GetMapping
    public List<AuditLogResponse> list() {
        return auditLogService.list().stream().map(AuditLogController::toResponse).toList();
    }

    @GetMapping("/{id}")
    public AuditLogResponse get(@PathVariable Long id) {
        return toResponse(auditLogService.get(id));
    }


    private static AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getUser() != null ? log.getUser().getId() : null,
                log.getAction(),
                log.getResource(),
                log.getTimestamp()
        );
    }
}
