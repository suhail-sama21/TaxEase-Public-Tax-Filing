package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.entityEnum.NotificationCategory;
import lombok.Data;

@Data
public class BroadcastRequest {
    private String message;
    private NotificationCategory category;
}