package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.ComplianceResponse;
import com.cognizant.taxease.dto.CreateComplianceRequest;
import com.cognizant.taxease.entity.ComplianceRecord;
import com.cognizant.taxease.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.cognizant.taxease.dto.UpdateComplianceRequest;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceService complianceService;

    @PostMapping
    public ComplianceResponse createCompliance(@RequestBody CreateComplianceRequest request) {
        return complianceService.createCompliance(request);
    }

    @GetMapping
    public List<ComplianceResponse> getAllCompliance() {
        return complianceService.getAllCompliance();
    }

    @GetMapping("/{id}")
    public ComplianceResponse getComplianceById(@PathVariable Long id){
        return complianceService.getComplianceById(id);
    }

    @PutMapping("/{id}")
    public ComplianceResponse updateCompliance(@PathVariable Long id, @RequestBody UpdateComplianceRequest request) {
        return complianceService.updateCompliance(id, request);
    }
}