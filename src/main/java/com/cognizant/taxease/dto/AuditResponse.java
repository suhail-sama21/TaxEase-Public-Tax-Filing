package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditResponse {

    private Long id;
    private Long officerId;
    private String scope;
    private String findings;
    private Instant createdAt;
    private StatusBasic status;
}