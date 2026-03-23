package com.cognizant.taxease.dto.responsedto;

import com.cognizant.taxease.entity.entityEnum.TaxpayerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxpayerProfileResponseDto {
    private Long taxpayerId;
    private String taxpayerIdNumber;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String contactInfo;
    private TaxpayerType type;
}