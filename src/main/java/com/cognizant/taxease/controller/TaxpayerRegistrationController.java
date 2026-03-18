package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.TaxpayerRegistrationRequest;
import com.cognizant.taxease.dto.TaxpayerRegistrationResponse;
import com.cognizant.taxease.exception.EmailAlreadyExistsException;
import com.cognizant.taxease.exception.TaxpayerIdGenerationException;
import com.cognizant.taxease.service.impl.TaxpayerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/taxpayer")
@RequiredArgsConstructor
public class TaxpayerRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(TaxpayerRegistrationController.class);

    private final TaxpayerRegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<TaxpayerRegistrationResponse> registerTaxpayer(@RequestBody TaxpayerRegistrationRequest request) {
        try {
            TaxpayerRegistrationResponse response = registrationService.registerTaxpayer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                TaxpayerRegistrationResponse.builder()
                    .message(e.getMessage())
                    .build()
            );
        } catch (TaxpayerIdGenerationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                TaxpayerRegistrationResponse.builder()
                    .message("Failed to generate unique taxpayer ID. Please try again.")
                    .build()
            );
        } catch (Exception e) {
            logger.error("Unexpected error during taxpayer registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                TaxpayerRegistrationResponse.builder()
                    .message("Registration failed due to an unexpected error.")
                    .build()
            );
        }
    }
}