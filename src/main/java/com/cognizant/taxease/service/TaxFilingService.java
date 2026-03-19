package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.TaxFilingRequestDTO;
import com.cognizant.taxease.dto.TaxFilingResponseDTO;
import java.util.List;

/**
 * Service contract for managing tax filings.
 */
public interface TaxFilingService {

    /**
     * Submit a new tax filing.
     * @param dto the filing data
     * @return the created filing response
     */
    TaxFilingResponseDTO submitFiling(TaxFilingRequestDTO dto);

    /**
     * Retrieve all filings for a specific taxpayer.
     * @param taxpayerId the ID of the taxpayer
     * @return list of filing history
     */
    List<TaxFilingResponseDTO> getFilingHistory(Long taxpayerId);

    /**
     * Update the status of a filing (Officer Action).
     * @param filingId the ID of the filing
     * @param newStatus the new status (APPROVED, REJECTED, etc.)
     * @param officerId the ID of the officer performing the update
     * @return the updated filing response
     */
    TaxFilingResponseDTO updateFilingStatus(Long filingId, String newStatus, Long officerId);
}