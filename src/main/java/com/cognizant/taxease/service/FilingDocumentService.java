package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.requestdto.FilingDocumentRequestDTO;
import com.cognizant.taxease.dto.responsedto.FilingDocumentResponseDTO;
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