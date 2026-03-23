package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.FilingDocumentRequestDTO;
import com.cognizant.taxease.dto.responsedto.FilingDocumentResponseDTO;
import com.cognizant.taxease.entity.FilingDocument;
import com.cognizant.taxease.service.FilingDocumentService;
import jakarta.validation.Valid; // Required to activate DTO validation
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class FilingDocumentController {

    private final FilingDocumentService documentService;

    /**
     * Uploads a document link for a specific filing.
     * Added @Valid to ensure the RequestDTO meets all validation constraints.
     */
    @PostMapping("/upload")
    public ResponseEntity<FilingDocumentResponseDTO> uploadDocument(
            @Valid @RequestBody FilingDocumentRequestDTO dto) {
        log.info("START: Uploading document for filing ID: {}", dto.getFilingId());
        FilingDocumentResponseDTO response = documentService.addDocument(dto);
        log.info("END: Document upload successful | ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/filing/{filingId}")
    public ResponseEntity<List<FilingDocumentResponseDTO>> getDocuments(@PathVariable Long filingId) {
        log.info("START: Fetching Specific documents by id : {}", filingId);
        List<FilingDocumentResponseDTO> fdr=documentService.getDocumentsByFiling(filingId);
        log.info("END: Successfully fetched documents for filingId: {}",filingId);
        return ResponseEntity.ok(fdr);
    }
}