package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.entityEnum.DocTypeTaxpayer;
import lombok.Data;

@Data
public class DocumentUploadRequestDto {
    private String fileUri;
    private DocTypeTaxpayer docType;
}