package com.cognizant.taxease.service;

import com.cognizant.taxease.dao.PaymentRepository;
import com.cognizant.taxease.dao.RevenueRecordRepository;
import com.cognizant.taxease.dao.TaxFilingRepository;
import com.cognizant.taxease.entity.Payment;
import com.cognizant.taxease.entity.TaxFiling;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.entityEnum.PaymentMethod;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private TaxFilingRepository taxFilingRepository;

    @Mock
    private RevenueRecordRepository revenueRecordRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private TaxFiling sampleFiling;
    private Taxpayer sampleTaxpayer;
    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        sampleTaxpayer = Taxpayer.builder()
                .id(1L)
                .build();

        sampleFiling = TaxFiling.builder()
                .id(100L)
                .taxpayer(sampleTaxpayer)
                .build();

        samplePayment = Payment.builder()
                .id(10L)
                .filing(sampleFiling)
                .amount(new BigDecimal("500.00"))
                .method(PaymentMethod.Wallet) // Updated to Wallet
                .status(StatusBasic.Pending)
                .build();
    }

    @Test
    void testMakePayment_Success_Completed() {
        // Arrange
        when(taxFilingRepository.findById(100L)).thenReturn(Optional.of(sampleFiling));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(20L); // simulate auto-generated ID
            return p;
        });

        // Act
        Payment result = paymentService.makePayment(100L, PaymentMethod.Bank, new BigDecimal("1000.00"), StatusBasic.Completed);

        // Assert
        assertNotNull(result);
        assertEquals(20L, result.getId());
        assertEquals(new BigDecimal("1000.00"), result.getAmount());
        assertEquals(StatusBasic.Completed, result.getStatus());
        assertEquals(PaymentMethod.Bank, result.getMethod()); // Updated to Bank

        // Verify audit log was recorded because status is Completed
        verify(auditLogService, times(1)).record("PAYMENT_CREATE", "payments/20");
    }

    @Test
    void testMakePayment_Success_Pending() {
        // Arrange
        when(taxFilingRepository.findById(100L)).thenReturn(Optional.of(sampleFiling));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Payment result = paymentService.makePayment(100L, PaymentMethod.Wallet, new BigDecimal("500.00"), StatusBasic.Pending);

        // Assert
        assertNotNull(result);
        assertEquals(StatusBasic.Pending, result.getStatus());
        assertEquals(PaymentMethod.Wallet, result.getMethod()); // Updated to Wallet

        // Verify audit log is NOT called because status is not Completed
        verify(auditLogService, never()).record(anyString(), anyString());
    }

    @Test
    void testMakePayment_FilingNotFound_ThrowsException() {
        // Arrange
        when(taxFilingRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.makePayment(999L, PaymentMethod.Bank, new BigDecimal("500.00"), StatusBasic.Pending);
        });

        assertEquals("Filing not found", exception.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetPaymentsByTaxpayer() {
        // Arrange
        Payment payment2 = Payment.builder().id(11L).filing(sampleFiling).build();
        when(paymentRepository.findByFiling_Taxpayer_Id(1L)).thenReturn(Arrays.asList(samplePayment, payment2));

        // Act
        List<Payment> payments = paymentService.getPaymentsByTaxpayer(1L);

        // Assert
        assertNotNull(payments);
        assertEquals(2, payments.size());
        verify(paymentRepository, times(1)).findByFiling_Taxpayer_Id(1L);
    }

    @Test
    void testRetryPayment_Success() {
        // Arrange
        Payment failedPayment = Payment.builder()
                .id(50L)
                .filing(sampleFiling)
                .amount(new BigDecimal("300.00"))
                .status(StatusBasic.Failed)
                .build();

        when(paymentRepository.findById(50L)).thenReturn(Optional.of(failedPayment));
        when(taxFilingRepository.findById(100L)).thenReturn(Optional.of(sampleFiling));

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment saved = invocation.getArgument(0);
            saved.setId(51L); // New payment ID
            return saved;
        });

        // Act
        Payment newPayment = paymentService.retryPayment(50L, PaymentMethod.Bank);

        // Assert
        assertNotNull(newPayment);
        assertEquals(StatusBasic.Completed, newPayment.getStatus());
        assertEquals(PaymentMethod.Bank, newPayment.getMethod()); // Updated to Bank
        assertEquals(new BigDecimal("300.00"), newPayment.getAmount());
        assertEquals(51L, newPayment.getId());

        verify(auditLogService, times(1)).record("PAYMENT_RETRY", "payments/50");
        verify(auditLogService, times(1)).record("PAYMENT_CREATE", "payments/51"); // Make payment also triggers this
    }

    @Test
    void testRetryPayment_OldPaymentNotFound_ThrowsException() {
        // Arrange
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.retryPayment(99L, PaymentMethod.Wallet);
        });

        assertEquals("Payment not found", exception.getMessage());
        verify(auditLogService, never()).record(anyString(), anyString());
    }

    @Test
    void testRetryPayment_OldPaymentNotFailed_ThrowsException() {
        // Arrange
        Payment pendingPayment = Payment.builder()
                .id(50L)
                .status(StatusBasic.Pending)
                .build();

        when(paymentRepository.findById(50L)).thenReturn(Optional.of(pendingPayment));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.retryPayment(50L, PaymentMethod.Wallet);
        });

        assertEquals("Only failed payments can be retried", exception.getMessage());
        verify(auditLogService, never()).record(anyString(), anyString());
    }
}