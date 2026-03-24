package com.cognizant.taxease.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.dao.TaxpayerRepository;
import com.cognizant.taxease.dao.UserRepository;
import com.cognizant.taxease.dto.requestdto.TaxFilingRequestDTO;
import com.cognizant.taxease.dto.responsedto.TaxFilingResponseDTO;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.service.impl.TaxFilingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaxFilingServiceTest {
    BigDecimal testAmount = new BigDecimal("50000.00");
    @Mock
    private TaxFilingRepository taxFilingRepository;

    @Mock
    private TaxpayerRepository taxpayerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private TaxFilingServiceImpl taxFilingService;

    private Taxpayer mockTaxpayer;
    private TaxFiling mockFiling;

    @BeforeEach
    void setUp() {

        mockTaxpayer = new Taxpayer();
        mockTaxpayer.setId(1L);

        mockFiling = TaxFiling.builder()
                .id(100L)
                .taxpayer(mockTaxpayer)
                .period("FY2025-26")
                .amountDeclared(testAmount)
                .status(StatusBasic.Pending)
                .build();
    }

    @Test
    void testSubmitFiling_Success() {
        // Arrange
        TaxFilingRequestDTO requestDTO = new TaxFilingRequestDTO();
        requestDTO.setTaxpayerId(1L);
        requestDTO.setPeriod("FY2025-26");
        requestDTO.setAmountDeclared(testAmount);

        when(taxpayerRepository.findById(1L)).thenReturn(Optional.of(mockTaxpayer));
        when(taxFilingRepository.save(any(TaxFiling.class))).thenReturn(mockFiling);

        // Act
        TaxFilingResponseDTO response = taxFilingService.submitFiling(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Pending", response.getStatus());
        verify(auditLogService, times(1)).record(eq("FILING_SUBMIT"), anyString());
        verify(taxFilingRepository, times(1)).save(any(TaxFiling.class));
    }

    @Test
    void testUpdateFilingStatus_Success() {
        // Arrange
        User mockOfficer = new User();
        mockOfficer.setId(10L);

        when(taxFilingRepository.findById(100L)).thenReturn(Optional.of(mockFiling));
        when(userRepository.findById(10L)).thenReturn(Optional.of(mockOfficer));
        when(taxFilingRepository.save(any(TaxFiling.class))).thenReturn(mockFiling);

        // Act
        TaxFilingResponseDTO response = taxFilingService.updateFilingStatus(100L, "Approved", 10L);

        // Assert
        assertNotNull(response);
        verify(auditLogService, times(1)).record(eq("FILING_STATUS_UPDATE"), anyString());
        assertEquals("Approved", response.getStatus());
    }

    @Test
    void testSubmitFiling_TaxpayerNotFound() {
        // Arrange
        TaxFilingRequestDTO requestDTO = new TaxFilingRequestDTO();
        requestDTO.setTaxpayerId(99L);
        when(taxpayerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taxFilingService.submitFiling(requestDTO));
        verify(taxFilingRepository, never()).save(any());
    }
}