package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.FilingDocumentRequestDTO;
import com.cognizant.taxease.dto.responsedto.FilingDocumentResponseDTO;
import com.cognizant.taxease.service.FilingDocumentService;
import jakarta.validation.Valid; // Required to activate DTO validation
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FilingDocumentController {

    private final FilingDocumentService documentService;

    /**
     * Uploads a document link for a specific filing.
     * Added @Valid to ensure the RequestDTO meets all validation constraints.
     */
    @PostMapping("/upload")
    public ResponseEntity<FilingDocumentResponseDTO> uploadDocument(
            @Valid @RequestBody FilingDocumentRequestDTO dto) {
        return new ResponseEntity<>(documentService.addDocument(dto), HttpStatus.CREATED);
    }

    @GetMapping("/filing/{filingId}")
    public ResponseEntity<List<FilingDocumentResponseDTO>> getDocuments(@PathVariable Long filingId) {
        return ResponseEntity.ok(documentService.getDocumentsByFiling(filingId));
    }
}