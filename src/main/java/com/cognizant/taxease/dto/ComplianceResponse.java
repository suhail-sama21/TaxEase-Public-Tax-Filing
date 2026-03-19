package com.cognizant.taxease.dto;
import com.cognizant.taxease.entity.entityEnum.ComplianceType;
import lombok.*;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceResponse {

    private Long id;
    private Long taxpayerId;
    private Long filingId;
    private Long paymentId;

    private ComplianceType type;
    private String result;
    private LocalDate date;
    private String notes;
    private Instant createdAt;
}
