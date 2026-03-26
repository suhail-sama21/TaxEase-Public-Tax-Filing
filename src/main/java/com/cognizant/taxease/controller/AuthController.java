package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.requestdto.LoginRequestDto;
import com.cognizant.taxease.dto.responsedto.LoginResponseDto;
import com.cognizant.taxease.dto.requestdto.TaxpayerRegistrationRequestDto;
import com.cognizant.taxease.dto.responsedto.TaxpayerRegistrationResponseDto;
import com.cognizant.taxease.service.AuthService;
import com.cognizant.taxease.service.impl.TaxpayerRegistrationService;
import jakarta.validation.Valid; // Required for validation enforcement
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {

    private final TaxpayerRegistrationService registrationService;
    private final AuthService authService;

    /**
     * Enforces @NotBlank and @Email rules from TaxpayerRegistrationRequestDto.
     */
    @PostMapping("/register")
    public ResponseEntity<TaxpayerRegistrationResponseDto> register(
            @Valid @RequestBody TaxpayerRegistrationRequestDto request) {
        log.info("START: Registering taxpayer with email: {}", request.getEmail());
        TaxpayerRegistrationResponseDto response = registrationService.registerTaxpayer(request);
        log.info("END: Registration successful for user ID: {}", response.getTaxpayerIdNumber());
        return ResponseEntity.ok(response);
    }

    /**
     * Enforces @NotBlank rules from LoginRequestDto.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {
        log.info("START: Login attempt for user: {}", request.getEmail());
        LoginResponseDto response = authService.login(request);
        log.info("END: Login successful ");
        return ResponseEntity.ok(response);
    }
}