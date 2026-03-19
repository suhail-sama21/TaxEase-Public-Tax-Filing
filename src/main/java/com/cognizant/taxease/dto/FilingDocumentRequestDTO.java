package com.cognizant.taxease.dto;

import lombok.Data;

@Data
public class FilingDocumentRequestDTO {
    private Long filingId;
    private String fileUrl; // In a real app, this would be an S3 bucket link or local path
}