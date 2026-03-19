package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.TaxFilingRequestDTO;
import com.cognizant.taxease.dto.TaxFilingResponseDTO;
import com.cognizant.taxease.service.impl.TaxFilingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaxFilingController {

    private final TaxFilingService taxFilingService;

    @PostMapping("/submit")
    public ResponseEntity<TaxFilingResponseDTO> submitFiling(@RequestBody TaxFilingRequestDTO dto) {
        return new ResponseEntity<>(taxFilingService.submitFiling(dto), HttpStatus.CREATED);
    }

    @GetMapping("/taxpayer/{taxpayerId}")
    public ResponseEntity<List<TaxFilingResponseDTO>> getHistory(@PathVariable Long taxpayerId) {
        // Change 'getFilingsByTaxpayer' to 'getFilingHistory'
        return ResponseEntity.ok(taxFilingService.getFilingHistory(taxpayerId));
    }

    @PutMapping("/{filingId}/status")
    public ResponseEntity<TaxFilingResponseDTO> updateStatus(
            @PathVariable Long filingId,
            @RequestParam String status,
            @RequestParam(required = false) Long officerId) {
        return ResponseEntity.ok(taxFilingService.updateFilingStatus(filingId, status, officerId));
    }
}