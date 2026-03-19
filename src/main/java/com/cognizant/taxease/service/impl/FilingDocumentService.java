package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dto.FilingDocumentRequestDTO;
import com.cognizant.taxease.dto.FilingDocumentResponseDTO;
import java.util.List;

/**
 * Service contract for managing filing documents.
 */
public interface FilingDocumentService {

    /**
     * Upload and link a document to a filing.
     */
    FilingDocumentResponseDTO addDocument(FilingDocumentRequestDTO dto);

    /**
     * Get all documents for a specific filing.
     */
    List<FilingDocumentResponseDTO> getDocumentsByFiling(Long filingId);
}