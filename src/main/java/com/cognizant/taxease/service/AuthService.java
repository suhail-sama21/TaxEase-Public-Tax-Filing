package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.requestdto.LoginRequestDto;
import com.cognizant.taxease.dto.responsedto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto dto);
}
