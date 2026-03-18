package com.cognizant.taxease.service;

import com.cognizant.taxease.entity.entityEnum.NotificationCategory;

public interface NotificationService {

    /**
     * Broadcasts a single notification message to all active users in the system.
     *
     * @param message  The content of the notification.
     * @param category The category of the notification (defaults to BROADCAST if null).
     */
    void broadcastNotification(String message, NotificationCategory category);

}