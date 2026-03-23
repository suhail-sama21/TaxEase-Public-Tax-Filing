package com.cognizant.taxease.service;

import com.cognizant.taxease.dao.AuditRepository;
import com.cognizant.taxease.dto.AuditResponse;
import com.cognizant.taxease.dto.CloseAuditRequest;
import com.cognizant.taxease.entity.Audit;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    void shouldReturnAllAudits() {
        // Arrange
        Audit audit1 = new Audit();
        audit1.setId(1L);
        audit1.setScope("Filing Check");
        audit1.setStatus(StatusBasic.Active);

        Audit audit2 = new Audit();
        audit2.setId(2L);
        audit2.setScope("Payment Check");
        audit2.setStatus(StatusBasic.Inactive);

        when(auditRepository.findAll()).thenReturn(List.of(audit1, audit2));

        // Act
        List<AuditResponse> responses = auditService.getAllAudits();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(2L, responses.get(1).getId());
        verify(auditRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAuditByIdSuccessfully() {
        // Arrange
        Long id = 10L;
        User officer = new User();
        officer.setId(5L);

        Audit audit = new Audit();
        audit.setId(id);
        audit.setOfficer(officer);
        audit.setScope("Internal Review");
        audit.setCreatedAt(Instant.now());

        when(auditRepository.findById(id)).thenReturn(Optional.of(audit));

        // Act
        AuditResponse response = auditService.getAuditById(id);

        // Assert
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(5L, response.getOfficerId());
        verify(auditLogService).record("AUDIT_VIEW", "audits/" + id);
    }

    @Test
    void shouldThrowException_whenAuditNotFound() {
        // Arrange
        when(auditRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> auditService.getAuditById(1L));
        verify(auditLogService, never()).record(anyString(), anyString());
    }

    @Test
    void shouldCloseAuditSuccessfully() {
        // Arrange
        Long id = 100L;
        Audit audit = new Audit();
        audit.setId(id);
        audit.setStatus(StatusBasic.Active);
        audit.setFindings("Initial findings");

        CloseAuditRequest request = new CloseAuditRequest();
        request.setFindings("Updated final findings");

        when(auditRepository.findById(id)).thenReturn(Optional.of(audit));

        // STUB SAVE: This prevents the NullPointerException when getId() is called on the result
        when(auditRepository.save(any(Audit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AuditResponse response = auditService.closeAudit(id, request);

        // Assert
        assertNotNull(response);
        assertEquals(StatusBasic.Inactive, response.getStatus());
        assertEquals("Updated final findings", response.getFindings());

        verify(auditRepository).save(audit);
        verify(auditLogService).record("AUDIT_CLOSE", "audits/" + id);
    }

    @Test
    void shouldCloseAuditWithoutChangingFindings_whenFindingsInRequestEmpty() {
        // Arrange
        Long id = 100L;
        Audit audit = new Audit();
        audit.setId(id);
        audit.setStatus(StatusBasic.Active);
        audit.setFindings("Do not change me");

        CloseAuditRequest request = new CloseAuditRequest();
        request.setFindings(""); // Empty findings string

        when(auditRepository.findById(id)).thenReturn(Optional.of(audit));
        when(auditRepository.save(any(Audit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AuditResponse response = auditService.closeAudit(id, request);

        // Assert
        assertEquals("Do not change me", response.getFindings());
        assertEquals(StatusBasic.Inactive, response.getStatus());
        verify(auditRepository).save(any(Audit.class));
    }
}