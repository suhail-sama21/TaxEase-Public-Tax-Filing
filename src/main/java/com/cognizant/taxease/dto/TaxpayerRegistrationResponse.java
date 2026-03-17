package com.cognizant.taxease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxpayerRegistrationResponse {
    private String taxpayerIdNumber; // 11-digit ID
    private String message;
    private String jwtToken;
}