package com.cognizant.taxease.service;

import com.cognizant.taxease.dto.LoginRequestDto;
import com.cognizant.taxease.dto.LoginResponseDto;
import com.cognizant.taxease.dto.SignUpRequestDto;
import com.cognizant.taxease.dto.SignUpResponseDto;

public interface AuthService {
    SignUpResponseDto signUp(SignUpRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);
}
