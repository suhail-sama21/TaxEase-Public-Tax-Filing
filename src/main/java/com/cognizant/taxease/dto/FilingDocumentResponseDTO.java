package com.cognizant.taxease.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class FilingDocumentResponseDTO {
    private Long id;
    private Long filingId;
    private String fileUrl;
    private Instant uploadedDate;
}