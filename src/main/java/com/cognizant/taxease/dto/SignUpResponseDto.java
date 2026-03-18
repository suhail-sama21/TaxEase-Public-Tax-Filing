package com.cognizant.taxease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponseDto {
    private Long userId;
    private String email;
}
