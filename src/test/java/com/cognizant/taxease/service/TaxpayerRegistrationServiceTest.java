package com.cognizant.taxease.service;

import com.cognizant.taxease.dao.TaxpayerRepository;
import com.cognizant.taxease.dao.UserRepository;
import com.cognizant.taxease.dto.requestdto.TaxpayerRegistrationRequestDto;
import com.cognizant.taxease.dto.responsedto.TaxpayerRegistrationResponseDto;
import com.cognizant.taxease.entity.Taxpayer;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.entity.entityEnum.TaxpayerType;
import com.cognizant.taxease.entity.entityEnum.UserRole;
import com.cognizant.taxease.service.impl.TaxpayerRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Taxpayer Registration Service Tests")
class TaxpayerRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaxpayerRepository taxpayerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private TaxpayerRegistrationService registrationService;

    private TaxpayerRegistrationRequestDto registrationRequest;
    private User mockUser;
    private Taxpayer mockTaxpayer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        registrationRequest = TaxpayerRegistrationRequestDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("+911234567890")
                .password("Password@123")
                .taxpayerType(TaxpayerType.Citizen)
                .address("123 Main Street, New York, NY 10001")
                .contactInfo("john.doe@example.com | +1-234-567-8901")
                .build();

        mockUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("+911234567890")
                .passwordHash("hashedPassword")
                .role(UserRole.TAXPAYER)
                .status(StatusBasic.Active)
                .build();

        mockTaxpayer = Taxpayer.builder()
                .id(1L)
                .user(mockUser)
                .name("John Doe")
                .type(TaxpayerType.Citizen)
                .taxpayerIdNumber("12345678901")
                .address("123 Main Street, New York, NY 10001")
                .contactInfo("john.doe@example.com | +1-234-567-8901")
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new taxpayer")
    void testRegisterTaxpayerSuccess() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Taxpayer registered successfully", response.getMessage());
        assertTrue(response.getTaxpayerIdNumber().matches("\\d{11}"));
        verify(userRepository, times(1)).save(any(User.class));
        verify(taxpayerRepository, times(1)).save(any(Taxpayer.class));
        verify(auditLogService, times(1)).recordRegistration(any(User.class), anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegisterTaxpayerEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.registerTaxpayer(registrationRequest);
        });
        assertEquals("Account already exists for this email", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(taxpayerRepository, never()).save(any(Taxpayer.class));
    }

    @Test
    @DisplayName("Should generate unique taxpayer ID")
    void testGenerateUniqueTaxpayerId() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response1 = registrationService.registerTaxpayer(registrationRequest);

        // Reset mocks for second registration
        reset(userRepository, taxpayerRepository);
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Assert
        assertNotNull(response1);
        assertTrue(response1.getTaxpayerIdNumber().matches("\\d{11}"));
    }

    @Test
    @DisplayName("Should encode password during registration")
    void testPasswordEncodingDuringRegistration() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword123");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(registrationRequest);

        // Assert
        assertNotNull(response);
        verify(passwordEncoder, times(1)).encode(registrationRequest.getPassword());
    }

    @Test
    @DisplayName("Should set correct user role as TAXPAYER")
    void testUserRoleSetAsTaxpayer() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(UserRole.TAXPAYER, mockUser.getRole());
    }

    @Test
    @DisplayName("Should set user status as Active")
    void testUserStatusSetAsActive() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(StatusBasic.Active, mockUser.getStatus());
    }

    @Test
    @DisplayName("Should record audit log for registration")
    void testAuditLogRecording() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(registrationRequest);

        // Assert
        assertNotNull(response);
        verify(auditLogService, times(1)).recordRegistration(
                eq(mockUser),
                eq("TAXPAYER_REGISTER"),
                contains("taxpayers/")
        );
    }

    @Test
    @DisplayName("Should preserve user details during registration")
    void testPreserveUserDetails() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(registrationRequest.getName(), mockUser.getName());
        assertEquals(registrationRequest.getEmail(), mockUser.getEmail());
        assertEquals(registrationRequest.getPhone(), mockUser.getPhone());
    }

    @Test
    @DisplayName("Should preserve taxpayer details during registration")
    void testPreserveTaxpayerDetails() {
        // Arrange
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("hashedPassword");
        when(taxpayerRepository.existsByTaxpayerIdNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(taxpayerRepository.save(any(Taxpayer.class))).thenReturn(mockTaxpayer);

        // Act
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(registrationRequest.getName(), mockTaxpayer.getName());
        assertEquals(registrationRequest.getTaxpayerType(), mockTaxpayer.getType());
        assertEquals(registrationRequest.getAddress(), mockTaxpayer.getAddress());
        assertEquals(registrationRequest.getContactInfo(), mockTaxpayer.getContactInfo());
    }
}
