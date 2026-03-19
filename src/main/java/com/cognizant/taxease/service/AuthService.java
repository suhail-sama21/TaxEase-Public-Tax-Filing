package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.LoginRequestDto;
import com.cognizant.taxease.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto dto);
}
