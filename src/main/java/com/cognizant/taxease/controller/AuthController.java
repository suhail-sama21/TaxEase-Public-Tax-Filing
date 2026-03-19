package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.LoginRequestDto;
import com.cognizant.taxease.dto.LoginResponseDto;
import com.cognizant.taxease.dto.TaxpayerRegistrationRequestDto;
import com.cognizant.taxease.dto.TaxpayerRegistrationResponseDto;
import com.cognizant.taxease.service.AuthService;
import com.cognizant.taxease.service.impl.TaxpayerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TaxpayerRegistrationService registrationService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TaxpayerRegistrationResponseDto> register(@RequestBody TaxpayerRegistrationRequestDto request) {

        return ResponseEntity.ok(registrationService.registerTaxpayer(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}