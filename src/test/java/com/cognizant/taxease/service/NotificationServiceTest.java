package com.cognizant.taxease.service;

import com.cognizant.taxease.dao.NotificationRepository;
import com.cognizant.taxease.dao.UserRepository;
import com.cognizant.taxease.dto.responsedto.NotificationResponse;
import com.cognizant.taxease.entity.Notification;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.NotificationCategory;
import com.cognizant.taxease.entity.entityEnum.NotificationStatus;
import com.cognizant.taxease.service.impl.NotificationServiceImpl; // Imported the implementation
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private ModelMapper modelMapper;

    // Injecting into the specific Implementation class
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Captor
    private ArgumentCaptor<List<Notification>> notificationListCaptor;

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    private User testUser;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@taxease.com");

        testNotification = Notification.builder()
                .id(100L)
                .user(testUser)
                .message("Test Message")
                .category(NotificationCategory.SYSTEM_UPDATE)
                .status(NotificationStatus.UNREAD)
                .build();
    }

    // ==========================================
    // Tests for broadcastNotification
    // ==========================================

    @Test
    void broadcastNotification_SuccessWithSpecificCategory() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        // Act
        notificationService.broadcastNotification("System downtime at midnight", NotificationCategory.SYSTEM_UPDATE);

        // Assert
        verify(notificationRepository, times(1)).saveAll(notificationListCaptor.capture());
        verify(auditLogService, times(1)).record("NOTIFICATION_BROADCAST", "notifications/bulk");

        List<Notification> savedNotifications = notificationListCaptor.getValue();
        assertEquals(1, savedNotifications.size());
        assertEquals("System downtime at midnight", savedNotifications.get(0).getMessage());
        assertEquals(NotificationCategory.SYSTEM_UPDATE, savedNotifications.get(0).getCategory());
        assertEquals(NotificationStatus.UNREAD, savedNotifications.get(0).getStatus());
    }

    @Test
    void broadcastNotification_FallbackToDefaultCategory() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        // Act: Pass null for category
        notificationService.broadcastNotification("Global message", null);

        // Assert
        verify(notificationRepository).saveAll(notificationListCaptor.capture());
        assertEquals(NotificationCategory.BROADCAST, notificationListCaptor.getValue().get(0).getCategory());
    }

    // ==========================================
    // Tests for markAsRead
    // ==========================================

    @Test
    void markAsRead_Success_WhenUnread() {
        // Arrange
        when(notificationRepository.findByIdAndUserId(100L, 1L)).thenReturn(Optional.of(testNotification));

        // Act
        notificationService.markAsRead(100L, 1L);

        // Assert
        assertEquals(NotificationStatus.READ, testNotification.getStatus());
        verify(notificationRepository, times(1)).save(testNotification);
        verify(auditLogService, times(1)).record("NOTIFICATION_SEND", "notifications/user/1");
    }

    @Test
    void markAsRead_NoOp_WhenAlreadyRead() {
        // Arrange
        testNotification.setStatus(NotificationStatus.READ);
        when(notificationRepository.findByIdAndUserId(100L, 1L)).thenReturn(Optional.of(testNotification));

        // Act
        notificationService.markAsRead(100L, 1L);

        // Assert
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(auditLogService, never()).record(anyString(), anyString());
    }

    @Test
    void markAsRead_ThrowsException_WhenNotFoundOrUnauthorized() {
        // Arrange
        when(notificationRepository.findByIdAndUserId(100L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.markAsRead(100L, 1L);
        });

        assertEquals("Notification not found or you do not have permission to access it.", exception.getMessage());
        verify(notificationRepository, never()).save(any());
    }

    // ==========================================
    // Tests for getUserNotifications
    // ==========================================

    @Test
    void getUserNotifications_Success() {
        // Arrange
        List<Notification> dbNotifications = List.of(testNotification);
        when(notificationRepository.findByUserIdOrderByCreatedDateDesc(1L)).thenReturn(dbNotifications);

        NotificationResponse mockResponse = NotificationResponse.builder()
                .id(100L)
                .message("Test Message")
                .build();
        List<NotificationResponse> expectedResponses = List.of(mockResponse);

        // Mock the ModelMapper TypeToken behavior
        when(modelMapper.map(eq(dbNotifications), any(java.lang.reflect.Type.class))).thenReturn(expectedResponses);

        // Act
        List<NotificationResponse> actualResponses = notificationService.getUserNotifications(1L);

        // Assert
        assertNotNull(actualResponses);
        assertEquals(1, actualResponses.size());
        assertEquals("Test Message", actualResponses.get(0).getMessage());
    }

    // ==========================================
    // Tests for sendNotificationToUser
    // ==========================================

    @Test
    void sendNotificationToUser_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        notificationService.sendNotificationToUser(1L, "Personal alert", NotificationCategory.FILING);

        // Assert
        verify(notificationRepository, times(1)).save(notificationCaptor.capture());
        Notification savedNotification = notificationCaptor.getValue();

        assertEquals(testUser, savedNotification.getUser());
        assertEquals("Personal alert", savedNotification.getMessage());
        assertEquals(NotificationCategory.FILING, savedNotification.getCategory());
        assertEquals(NotificationStatus.UNREAD, savedNotification.getStatus());
    }

    @Test
    void sendNotificationToUser_FallbackToDefaultCategory() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act: Pass null for category
        notificationService.sendNotificationToUser(1L, "Personal alert", null);

        // Assert
        verify(notificationRepository).save(notificationCaptor.capture());
        assertEquals(NotificationCategory.SYSTEM_UPDATE, notificationCaptor.getValue().getCategory());
    }

    @Test
    void sendNotificationToUser_ThrowsException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.sendNotificationToUser(99L, "Failed alert", NotificationCategory.FILING);
        });

        assertEquals("User not found with ID: 99", exception.getMessage());
        verify(notificationRepository, never()).save(any());
    }
}