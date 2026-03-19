package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.FilingDocumentRequestDTO;
import com.cognizant.taxease.dto.FilingDocumentResponseDTO;
import com.cognizant.taxease.entity.FilingDocument;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.dao.FilingDocumentRepository;
import com.cognizant.taxease.dao.TaxFilingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilingDocumentService {

    private final FilingDocumentRepository documentRepository;
    private final TaxFilingRepository taxFilingRepository;

    @Transactional
    public FilingDocumentResponseDTO addDocument(FilingDocumentRequestDTO dto) {
        TaxFiling filing = taxFilingRepository.findById(dto.getFilingId())
                .orElseThrow(() -> new RuntimeException("Filing not found with ID: " + dto.getFilingId()));

        FilingDocument document = FilingDocument.builder()
                .filing(filing)
                .fileUrl(dto.getFileUrl())
                // docType and verificationStatus can be added here once you build those enums
                .build();

        FilingDocument savedDoc = documentRepository.save(document);

        return FilingDocumentResponseDTO.builder()
                .id(savedDoc.getId())
                .filingId(savedDoc.getFiling().getId())
                .fileUrl(savedDoc.getFileUrl())
                .uploadedDate(savedDoc.getUploadedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<FilingDocumentResponseDTO> getDocumentsByFiling(Long filingId) {
        return documentRepository.findByFilingId(filingId).stream()
                .map(doc -> FilingDocumentResponseDTO.builder()
                        .id(doc.getId())
                        .filingId(doc.getFiling().getId())
                        .fileUrl(doc.getFileUrl())
                        .uploadedDate(doc.getUploadedDate())
                        .build())
                .collect(Collectors.toList());
    }
}