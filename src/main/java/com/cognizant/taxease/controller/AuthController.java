package com.cognizant.taxease.controller;

import com.cognizant.taxease.dto.LoginRequestDto;
import com.cognizant.taxease.dto.LoginResponseDto;
import com.cognizant.taxease.dto.SignUpRequestDto;
import com.cognizant.taxease.dto.SignUpResponseDto;
import com.cognizant.taxease.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto dto) {
        SignUpResponseDto response = authService.signUp(dto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/taxpayer/register")
    public String register(){
        return "It is taxpayer";
    }

}
