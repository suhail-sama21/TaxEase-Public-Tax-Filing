package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AuditLogResponse {
    private Long id;
    private Long userId;
    private String action;
    private String resource;
    private Instant timestamp;
}