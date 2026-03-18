package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dao.NotificationRepository;
import com.cognizant.taxease.dao.UserRepository;
import com.cognizant.taxease.entity.Notification;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.NotificationCategory;
import com.cognizant.taxease.entity.entityEnum.NotificationStatus;
import com.cognizant.taxease.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void broadcastNotification(String message, NotificationCategory category) {
        // Fetch all users
        List<User> allUsers = userRepository.findAll();

        // Map users to new Notification entities
        List<Notification> notifications = allUsers.stream().map(user ->
                Notification.builder()
                        .user(user)
                        .message(message)
                        .category(category != null ? category : NotificationCategory.BROADCAST)
                        .status(NotificationStatus.UNREAD)
                        .build()
        ).collect(Collectors.toList());

        // Save all notifications to the database in one batch
        notificationRepository.saveAll(notifications);
    }
}