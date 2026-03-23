package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dto.FilingDocumentRequestDTO;
import com.cognizant.taxease.dto.FilingDocumentResponseDTO;
import com.cognizant.taxease.entity.FilingDocument;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.dao.FilingDocumentRepository;
import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.service.AuditLogService;
import com.cognizant.taxease.service.FilingDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilingDocumentServiceImpl implements FilingDocumentService {

    private final FilingDocumentRepository documentRepository;
    private final TaxFilingRepository taxFilingRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public FilingDocumentResponseDTO addDocument(FilingDocumentRequestDTO dto) {
        TaxFiling filing = taxFilingRepository.findById(dto.getFilingId())
                .orElseThrow(() -> new RuntimeException("Filing not found"));

        FilingDocument document = FilingDocument.builder()
                .filing(filing)
                .fileUrl(dto.getFileUrl())
                .build();

        FilingDocument savedDoc = documentRepository.save(document);

        auditLogService.record("DOCUMENT_UPLOAD", "filing_documents/" + savedDoc.getId());
        return FilingDocumentResponseDTO.builder()
                .id(savedDoc.getId())
                .filingId(savedDoc.getFiling().getId())
                .fileUrl(savedDoc.getFileUrl())
                .uploadedDate(savedDoc.getUploadedDate())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilingDocumentResponseDTO> getDocumentsByFiling(Long filingId) {
        auditLogService.record("DOCUMENT_LIST_VIEW", "filings/" + filingId + "/documents");
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