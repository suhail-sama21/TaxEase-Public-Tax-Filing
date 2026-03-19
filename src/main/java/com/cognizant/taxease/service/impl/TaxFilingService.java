package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dao.UserRepository;
import com.cognizant.taxease.dto.TaxFilingRequestDTO;
import com.cognizant.taxease.dto.TaxFilingResponseDTO;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.dao.TaxpayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxFilingService {

    private final TaxFilingRepository taxFilingRepository;
    private final TaxpayerRepository taxpayerRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaxFilingResponseDTO submitFiling(TaxFilingRequestDTO requestDTO) {
        // 1. Fetch the actual Taxpayer entity
        Taxpayer taxpayer = taxpayerRepository.findById(requestDTO.getTaxpayerId())
                .orElseThrow(() -> new RuntimeException("Taxpayer not found with ID: " + requestDTO.getTaxpayerId()));

        // 2. Build the entity using your Lombok builder
        TaxFiling filing = TaxFiling.builder()
                .taxpayer(taxpayer)
                .period(requestDTO.getPeriod())
                .amountDeclared(requestDTO.getAmountDeclared())
                .status(StatusBasic.Pending)
                .build();

        // 3. Save to DB
        TaxFiling savedFiling = taxFilingRepository.save(filing);

        // 4. Map back to DTO
        return mapToDTO(savedFiling);
    }

    @Transactional(readOnly = true)
    public List<TaxFilingResponseDTO> getFilingsByTaxpayer(Long taxpayerId) {
        return taxFilingRepository.findByTaxpayerId(taxpayerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to map Entity -> DTO
    private TaxFilingResponseDTO mapToDTO(TaxFiling filing) {
        return TaxFilingResponseDTO.builder()
                .id(filing.getId())
                .taxpayerId(filing.getTaxpayer().getId())
                .period(filing.getPeriod())
                .amountDeclared(filing.getAmountDeclared())
                .status(filing.getStatus().name())
                .submittedDate(filing.getSubmittedDate())
                .build();
    }
    // Add this method to your existing TaxFilingService.java
    // Don't forget to add: private final UserRepository userRepository; at the top!

    @Transactional
    public TaxFilingResponseDTO updateFilingStatus(Long filingId, String newStatus, Long officerId) {
        TaxFiling filing = taxFilingRepository.findById(filingId)
                .orElseThrow(() -> new RuntimeException("Filing not found with ID: " + filingId));

        // Update the status
        filing.setStatus(StatusBasic.valueOf(newStatus));

        // If an officer is approving this, attach them to the record
        if (officerId != null) {
            com.cognizant.taxease.entity.User officer = userRepository.findById(officerId)
                    .orElseThrow(() -> new RuntimeException("Officer not found with ID: " + officerId));
            filing.setOfficer(officer);
        }

        TaxFiling updatedFiling = taxFilingRepository.save(filing);
        return mapToDTO(updatedFiling);
    }
}
