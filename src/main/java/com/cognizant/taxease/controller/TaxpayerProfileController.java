package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.DocumentUpdateRequestDto;
import com.cognizant.taxease.dto.requestdto.DocumentUploadRequestDto;
import com.cognizant.taxease.dto.requestdto.UpdateTaxpayerProfileRequestDto;
import com.cognizant.taxease.dto.responsedto.TaxpayerDocumentResponseDto;
import com.cognizant.taxease.dto.responsedto.TaxpayerProfileResponseDto;
import com.cognizant.taxease.service.TaxpayerProfileService;
import jakarta.validation.Valid; // Required for validation enforcement
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxpayers")
@RequiredArgsConstructor
public class TaxpayerProfileController {

    private final TaxpayerProfileService taxpayerProfileService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<TaxpayerProfileResponseDto> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TaxpayerProfileResponseDto profile = taxpayerProfileService.getProfile(email);
        return ResponseEntity.ok(profile);
    }

    /**
     * Updates taxpayer profile.
     * @Valid ensures phone numbers, names, and addresses meet DTO requirements.
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<TaxpayerProfileResponseDto> updateProfile(
            @Valid @RequestBody UpdateTaxpayerProfileRequestDto request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TaxpayerProfileResponseDto updatedProfile = taxpayerProfileService.updateProfile(email, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/documents")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<List<TaxpayerDocumentResponseDto>> getDocuments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TaxpayerDocumentResponseDto> documents = taxpayerProfileService.getDocuments(email);
        return ResponseEntity.ok(documents);
    }

    /**
     * Uploads a profile document.
     * @Valid ensures the file URI and document type are provided.
     */
    @PostMapping("/documents/upload")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<TaxpayerDocumentResponseDto> uploadDocument(
            @Valid @RequestBody DocumentUploadRequestDto request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TaxpayerDocumentResponseDto document = taxpayerProfileService.uploadDocument(email, request.getFileUri(), request.getDocType());
        return ResponseEntity.ok(document);
    }

    @DeleteMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        taxpayerProfileService.deleteDocument(email, documentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing document (e.g., updating the file link).
     */
    @PutMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<TaxpayerDocumentResponseDto> updateDocument(
            @PathVariable Long documentId,
            @Valid @RequestBody DocumentUpdateRequestDto request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TaxpayerDocumentResponseDto updatedDocument = taxpayerProfileService.updateDocument(email, documentId, request.getFileUri());
        return ResponseEntity.ok(updatedDocument);
    }
}