package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.BroadcastRequest;
import com.cognizant.taxease.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestBody BroadcastRequest request) {
        notificationService.broadcastNotification(request.getMessage(), request.getCategory());
        return ResponseEntity.ok("Broadcast notification sent successfully to all users.");
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markNotificationAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {

        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok("Notification successfully marked as read.");
    }
}