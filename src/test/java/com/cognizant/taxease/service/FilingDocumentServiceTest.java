package com.cognizant.taxease.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cognizant.taxease.dao.FilingDocumentRepository;
import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.dto.requestdto.FilingDocumentRequestDTO;
import com.cognizant.taxease.dto.responsedto.FilingDocumentResponseDTO;
import com.cognizant.taxease.entity.FilingDocument;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.service.impl.FilingDocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FilingDocumentServiceTest {

    @Mock
    private FilingDocumentRepository documentRepository;

    @Mock
    private TaxFilingRepository taxFilingRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private FilingDocumentServiceImpl filingDocumentService;

    private TaxFiling mockFiling;
    private FilingDocument mockDocument;

    @BeforeEach
    void setUp() {
        mockFiling = new TaxFiling();
        mockFiling.setId(50L);

        mockDocument = FilingDocument.builder()
                .id(1L)
                .filing(mockFiling)
                .fileUrl("https://taxease.storage/docs/test.pdf")
                .uploadedDate(Instant.now())
                .build();
    }

    @Test
    void testAddDocument_Success() {
        // Arrange
        FilingDocumentRequestDTO requestDTO = new FilingDocumentRequestDTO();
        requestDTO.setFilingId(50L);
        requestDTO.setFileUrl("https://taxease.storage/docs/test.pdf");

        when(taxFilingRepository.findById(50L)).thenReturn(Optional.of(mockFiling));
        when(documentRepository.save(any(FilingDocument.class))).thenReturn(mockDocument);

        // Act
        FilingDocumentResponseDTO response = filingDocumentService.addDocument(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("https://taxease.storage/docs/test.pdf", response.getFileUrl());
        verify(auditLogService, times(1)).record(eq("DOCUMENT_UPLOAD"), anyString());
        verify(documentRepository).save(any(FilingDocument.class));
    }

    @Test
    void testAddDocument_FilingNotFound() {
        // Arrange
        FilingDocumentRequestDTO requestDTO = new FilingDocumentRequestDTO();
        requestDTO.setFilingId(999L);
        when(taxFilingRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                filingDocumentService.addDocument(requestDTO));

        assertEquals("Filing not found", exception.getMessage());
        verify(documentRepository, never()).save(any());
    }

    @Test
    void testGetDocumentsByFiling_Success() {
        // Arrange
        when(documentRepository.findByFilingId(50L)).thenReturn(List.of(mockDocument));

        // Act
        List<FilingDocumentResponseDTO> result = filingDocumentService.getDocumentsByFiling(50L);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(50L, result.get(0).getFilingId());
        verify(auditLogService).record(eq("DOCUMENT_LIST_VIEW"), contains("50"));
    }
}