package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.CreateComplianceRequest;
import com.cognizant.taxease.entity.ComplianceRecord;
import com.cognizant.taxease.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceService complianceService;

    @PostMapping
    public ComplianceRecord createCompliance(@RequestBody CreateComplianceRequest request) {
        return complianceService.createCompliance(request);
    }

    @GetMapping
    public List<ComplianceRecord> getAllCompliance() {
        return complianceService.getAllCompliance();
    }
}