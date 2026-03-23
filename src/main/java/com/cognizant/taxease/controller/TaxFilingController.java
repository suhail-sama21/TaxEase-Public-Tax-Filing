package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.TaxFilingRequestDTO;
import com.cognizant.taxease.dto.responsedto.TaxFilingResponseDTO;
import com.cognizant.taxease.service.TaxFilingService;
import jakarta.validation.Valid; // Required for DTO validation
import jakarta.validation.constraints.NotBlank; // For parameter validation
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated; // Required for @RequestParam validation
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated // Enables validation for @RequestParam
public class TaxFilingController {

    private final TaxFilingService taxFilingService;

    /**
     * Submits a new tax filing.
     * @Valid activates the rules like @Positive on income and @NotNull on taxYear.
     */
    @PostMapping("/submit")
    public ResponseEntity<TaxFilingResponseDTO> submitFiling(
            @Valid @RequestBody TaxFilingRequestDTO dto) {
        return new ResponseEntity<>(taxFilingService.submitFiling(dto), HttpStatus.CREATED);
    }

    @GetMapping("/taxpayer/{taxpayerId}")
    public ResponseEntity<List<TaxFilingResponseDTO>> getHistory(@PathVariable Long taxpayerId) {
        return ResponseEntity.ok(taxFilingService.getFilingHistory(taxpayerId));
    }

    /**
     * Updates the status of a filing (e.g., to APPROVED or REJECTED).
     * @NotBlank ensures the status isn't an empty string.
     */
    @PutMapping("/{filingId}/status")
    public ResponseEntity<TaxFilingResponseDTO> updateStatus(
            @PathVariable Long filingId,
            @NotBlank(message = "Status is required") @RequestParam String status,
            @RequestParam(required = false) Long officerId) {
        return ResponseEntity.ok(taxFilingService.updateFilingStatus(filingId, status, officerId));
    }
}