package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.entityEnum.NotificationCategory;
import com.cognizant.taxease.entity.entityEnum.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String message;
    private NotificationCategory category;
    private NotificationStatus status;
    private Long entityId;
    private Instant createdDate;
}