package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.LoginRequestDto;
import com.cognizant.taxease.dto.responsedto.LoginResponseDto;
import com.cognizant.taxease.dto.requestdto.TaxpayerRegistrationRequestDto;
import com.cognizant.taxease.dto.responsedto.TaxpayerRegistrationResponseDto;
import com.cognizant.taxease.service.AuthService;
import com.cognizant.taxease.service.impl.TaxpayerRegistrationService;
import jakarta.validation.Valid; // Required for validation enforcement
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TaxpayerRegistrationService registrationService;
    private final AuthService authService;

    /**
     * Enforces @NotBlank and @Email rules from TaxpayerRegistrationRequestDto.
     */
    @PostMapping("/register")
    public ResponseEntity<TaxpayerRegistrationResponseDto> register(
            @Valid @RequestBody TaxpayerRegistrationRequestDto request) {
        return ResponseEntity.ok(registrationService.registerTaxpayer(request));
    }

    /**
     * Enforces @NotBlank rules from LoginRequestDto.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}