package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.DocumentUpdateRequestDto;
import com.cognizant.taxease.dto.DocumentUploadRequestDto;
import com.cognizant.taxease.dto.TaxpayerDocumentResponseDto;
import com.cognizant.taxease.dto.TaxpayerProfileResponseDto;
import com.cognizant.taxease.dto.UpdateTaxpayerProfileRequestDto;
import com.cognizant.taxease.service.TaxpayerProfileService;
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

    @PutMapping("/profile")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<TaxpayerProfileResponseDto> updateProfile(@RequestBody UpdateTaxpayerProfileRequestDto request) {
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

    @PostMapping("/documents/upload")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<TaxpayerDocumentResponseDto> uploadDocument(@RequestBody DocumentUploadRequestDto request) {
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

    @PutMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('TAXPAYER')")
    public ResponseEntity<TaxpayerDocumentResponseDto> updateDocument(
            @PathVariable Long documentId,
            @RequestBody DocumentUpdateRequestDto request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TaxpayerDocumentResponseDto updatedDocument = taxpayerProfileService.updateDocument(email, documentId, request.getFileUri());
        return ResponseEntity.ok(updatedDocument);
    }
}