package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.entityEnum.NotificationCategory;
import com.cognizant.taxease.entity.entityEnum.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String message;
    private NotificationCategory category;
    private NotificationStatus status;
    private Long entityId; // Optional: To create a clickable link in the frontend
    private Instant createdDate;
}