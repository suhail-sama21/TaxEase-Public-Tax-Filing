package com.cognizant.taxease.service;

import com.cognizant.taxease.dao.*;
import com.cognizant.taxease.dto.responsedto.ComplianceResponse;
import com.cognizant.taxease.dto.requestdto.CreateComplianceRequest;
import com.cognizant.taxease.dto.requestdto.UpdateComplianceRequest;
import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.ComplianceRecord;
import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.ComplianceType;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.service.impl.ComplianceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceServiceImplTest {

    @Mock
    private ComplianceRecordRepository complianceRecordRepository;

    @Mock
    private TaxpayerRepository taxpayerRepository;

    @Mock
    private TaxFilingRepository taxFilingRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private UserRepository userRepository;

    // FIX: Replaced AuditLogRepository with AuditLogService
    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private ComplianceServiceImpl complianceService;

    @Test
    void shouldCreateComplianceSuccessfully_forFiling() {
        CreateComplianceRequest request = new CreateComplianceRequest();
        request.setTaxpayerId(1L);
        request.setFilingId(10L);
        request.setType(ComplianceType.Filing);
        request.setResult("Compliant");
        request.setNotes("All good");

        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setId(1L);

        TaxFiling filing = new TaxFiling();
        filing.setId(10L);

        when(taxpayerRepository.findById(1L)).thenReturn(Optional.of(taxpayer));
        when(taxFilingRepository.findById(10L)).thenReturn(Optional.of(filing));

        when(complianceRecordRepository.save(any(ComplianceRecord.class)))
                .thenAnswer(invocation -> {
                    ComplianceRecord saved = invocation.getArgument(0);
                    saved.setId(100L);
                    saved.setCreatedAt(Instant.now());
                    if (saved.getDate() == null) {
                        saved.setDate(LocalDate.now());
                    }
                    return saved;
                });

        ComplianceResponse response = complianceService.createCompliance(request);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(1L, response.getTaxpayerId());
        assertEquals(10L, response.getFilingId());
        assertNull(response.getPaymentId());
        assertEquals(ComplianceType.Filing, response.getType());
        assertEquals("Compliant", response.getResult());
        assertEquals("All good", response.getNotes());

        verify(complianceRecordRepository, times(1)).save(any(ComplianceRecord.class));
        verify(paymentRepository, never()).findById(any());
    }

    @Test
    void shouldCreateComplianceSuccessfully_forPayment() {
        CreateComplianceRequest request = new CreateComplianceRequest();
        request.setTaxpayerId(1L);
        request.setPaymentId(20L);
        request.setType(ComplianceType.Payment);
        request.setResult("Non-Compliant");
        request.setNotes("Payment issue");

        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setId(1L);

        Payment payment = new Payment();
        payment.setId(20L);

        when(taxpayerRepository.findById(1L)).thenReturn(Optional.of(taxpayer));
        when(paymentRepository.findById(20L)).thenReturn(Optional.of(payment));

        when(complianceRecordRepository.save(any(ComplianceRecord.class)))
                .thenAnswer(invocation -> {
                    ComplianceRecord saved = invocation.getArgument(0);
                    saved.setId(101L);
                    saved.setCreatedAt(Instant.now());
                    if (saved.getDate() == null) {
                        saved.setDate(LocalDate.now());
                    }
                    return saved;
                });

        ComplianceResponse response = complianceService.createCompliance(request);

        assertNotNull(response);
        assertEquals(101L, response.getId());
        assertEquals(1L, response.getTaxpayerId());
        assertNull(response.getFilingId());
        assertEquals(20L, response.getPaymentId());
        assertEquals(ComplianceType.Payment, response.getType());
        assertEquals("Non-Compliant", response.getResult());

        verify(complianceRecordRepository).save(any(ComplianceRecord.class));
        verify(taxFilingRepository, never()).findById(any());
    }

    @Test
    void shouldThrowException_whenTaxpayerNotFound() {
        CreateComplianceRequest request = new CreateComplianceRequest();
        request.setTaxpayerId(1L);
        request.setFilingId(10L);
        request.setType(ComplianceType.Filing);

        when(taxpayerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> complianceService.createCompliance(request));

        verify(complianceRecordRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_whenFilingIdMissingForFilingType() {
        CreateComplianceRequest request = new CreateComplianceRequest();
        request.setTaxpayerId(1L);
        request.setType(ComplianceType.Filing);
        request.setResult("Compliant");

        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setId(1L);

        when(taxpayerRepository.findById(1L)).thenReturn(Optional.of(taxpayer));

        assertThrows(IllegalArgumentException.class, () -> complianceService.createCompliance(request));

        verify(complianceRecordRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_whenPaymentIdMissingForPaymentType() {
        CreateComplianceRequest request = new CreateComplianceRequest();
        request.setTaxpayerId(1L);
        request.setType(ComplianceType.Payment);
        request.setResult("Compliant");

        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setId(1L);

        when(taxpayerRepository.findById(1L)).thenReturn(Optional.of(taxpayer));

        assertThrows(IllegalArgumentException.class, () -> complianceService.createCompliance(request));

        verify(complianceRecordRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllCompliance() {
        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setId(1L);

        ComplianceRecord record1 = new ComplianceRecord();
        record1.setId(1L);
        record1.setTaxpayer(taxpayer);
        record1.setType(ComplianceType.Filing);
        record1.setResult("Compliant");
        record1.setDate(LocalDate.now());
        record1.setCreatedAt(Instant.now());

        ComplianceRecord record2 = new ComplianceRecord();
        record2.setId(2L);
        record2.setTaxpayer(taxpayer);
        record2.setType(ComplianceType.Payment);
        record2.setResult("Non-Compliant");
        record2.setDate(LocalDate.now());
        record2.setCreatedAt(Instant.now());

        when(complianceRecordRepository.findAll()).thenReturn(List.of(record1, record2));

        List<ComplianceResponse> responses = complianceService.getAllCompliance();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(2L, responses.get(1).getId());
    }

    @Test
    void shouldReturnComplianceById() {
        Long id = 1L;

        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setId(1L);

        ComplianceRecord record = new ComplianceRecord();
        record.setId(id);
        record.setTaxpayer(taxpayer);
        record.setType(ComplianceType.Filing);
        record.setResult("Compliant");
        record.setNotes("Checked");
        record.setDate(LocalDate.now());
        record.setCreatedAt(Instant.now());

        when(complianceRecordRepository.findById(id)).thenReturn(Optional.of(record));

        ComplianceResponse response = complianceService.getComplianceById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(1L, response.getTaxpayerId());
        assertEquals("Compliant", response.getResult());
    }

    @Test
    void shouldThrowException_whenComplianceNotFoundById() {
        when(complianceRecordRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> complianceService.getComplianceById(1L));
    }

    @Test
    void shouldUpdateComplianceSuccessfully_withoutAuditTrigger() {
        Long id = 1L;

        ComplianceRecord record = new ComplianceRecord();
        record.setId(id);
        record.setResult("Compliant");
        record.setNotes("Old notes");
        record.setCreatedAt(Instant.now());

        UpdateComplianceRequest request = new UpdateComplianceRequest();
        request.setResult("Compliant");
        request.setNotes("Updated notes");

        when(complianceRecordRepository.findById(id)).thenReturn(Optional.of(record));
        when(complianceRecordRepository.save(any(ComplianceRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ComplianceResponse response = complianceService.updateCompliance(id, request);

        assertNotNull(response);
        assertEquals("Compliant", response.getResult());
        assertEquals("Updated notes", response.getNotes());

        verify(auditRepository, never()).save(any(Audit.class));
        verify(complianceRecordRepository).save(record);
    }

    @Test
    void shouldTriggerAudit_whenComplianceUpdatedToNonCompliant() {
        Long id = 1L;

        ComplianceRecord record = new ComplianceRecord();
        record.setId(id);
        record.setResult("Compliant");
        record.setNotes("Mismatch found");
        record.setCreatedAt(Instant.now());

        UpdateComplianceRequest request = new UpdateComplianceRequest();
        request.setResult("Non-Compliant");
        request.setNotes("Mismatch found");

        User officer = User.builder()
                .name("Officer")
                .email("officer@test.com")
                .passwordHash("hashed")
                .role(com.cognizant.taxease.entity.entityEnum.UserRole.OFFICER)
                .status(StatusBasic.Active)
                .build();
        officer.setId(1L);

        when(complianceRecordRepository.findById(id)).thenReturn(Optional.of(record));
        when(userRepository.findById(1L)).thenReturn(Optional.of(officer));
        when(complianceRecordRepository.save(any(ComplianceRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Stub the audit save to return the object instead of null
        when(auditRepository.save(any(Audit.class))).thenAnswer(invocation -> {
            Audit savedAudit = invocation.getArgument(0);
            savedAudit.setId(500L);
            return savedAudit;
        });

        ComplianceResponse response = complianceService.updateCompliance(id, request);

        assertNotNull(response);
        assertEquals("Non-Compliant", response.getResult());

        ArgumentCaptor<Audit> auditCaptor = ArgumentCaptor.forClass(Audit.class);
        verify(auditRepository, times(1)).save(auditCaptor.capture());

        Audit capturedAudit = auditCaptor.getValue();
        assertNotNull(capturedAudit);
        assertEquals(officer, capturedAudit.getOfficer());
        assertTrue(capturedAudit.getScope().contains(String.valueOf(id)));
        assertEquals("Mismatch found", capturedAudit.getFindings());
    }

    @Test
    void shouldReturnComplianceByTaxpayerId() {
        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setId(1L);

        ComplianceRecord record = new ComplianceRecord();
        record.setId(1L);
        record.setTaxpayer(taxpayer);
        record.setType(ComplianceType.Filing);
        record.setResult("Compliant");
        record.setCreatedAt(Instant.now());

        when(complianceRecordRepository.findByTaxpayer_Id(1L)).thenReturn(List.of(record));

        List<ComplianceResponse> responses = complianceService.getComplianceByTaxpayerId(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getTaxpayerId());
    }

    @Test
    void shouldReturnComplianceByResultIgnoreCase() {
        ComplianceRecord record = new ComplianceRecord();
        record.setId(1L);
        record.setResult("Compliant");
        record.setCreatedAt(Instant.now());

        when(complianceRecordRepository.findByResultIgnoreCase("Compliant"))
                .thenReturn(List.of(record));

        List<ComplianceResponse> responses = complianceService.getByResult("Compliant");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Compliant", responses.get(0).getResult());
    }
}