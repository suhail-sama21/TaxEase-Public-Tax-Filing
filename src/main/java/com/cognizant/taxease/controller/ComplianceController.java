package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.responsedto.ComplianceResponse;
import com.cognizant.taxease.dto.requestdto.CreateComplianceRequest;
import com.cognizant.taxease.dto.requestdto.UpdateComplianceRequest;
import com.cognizant.taxease.service.ComplianceService;
import jakarta.validation.Valid; // Required for validation enforcement
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceService complianceService;

    @PostMapping
    public ResponseEntity<ComplianceResponse> createCompliance(
            @Valid @RequestBody CreateComplianceRequest request) {
        return ResponseEntity.ok(complianceService.createCompliance(request));
    }

    @GetMapping
    public ResponseEntity<List<ComplianceResponse>> getAllCompliance() {
        return ResponseEntity.ok(complianceService.getAllCompliance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplianceResponse> getComplianceById(@PathVariable Long id){
        return ResponseEntity.ok(complianceService.getComplianceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplianceResponse> updateCompliance(
            @PathVariable Long id,
            @Valid @RequestBody UpdateComplianceRequest request) {
        return ResponseEntity.ok(complianceService.updateCompliance(id, request));
    }

    @GetMapping("/taxpayer/{taxpayerId}")
    public ResponseEntity<List<ComplianceResponse>> getComplianceByTaxpayerId(@PathVariable Long taxpayerId){
        return ResponseEntity.ok(complianceService.getComplianceByTaxpayerId(taxpayerId));
    }

    @GetMapping("/result/{result}")
    public ResponseEntity<List<ComplianceResponse>> getByResult(@PathVariable String result){
        return ResponseEntity.ok(complianceService.getByResult(result));
    }
}