package com.cognizant.taxease.dto;

import com.cognizant.taxease.entity.entityEnum.TaxpayerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxpayerRegistrationRequestDto {

    private String name;
    private String email;
    private String phone;
    private String password;

    private TaxpayerType taxpayerType;   // Citizen / Business
    private String address;
    private String contactInfo;

}