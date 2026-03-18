package com.cognizant.taxease.service.impl;

import com.cognizant.taxease.dto.LoginRequestDto;
import com.cognizant.taxease.dto.LoginResponseDto;
import com.cognizant.taxease.dto.SignUpRequestDto;
import com.cognizant.taxease.dto.SignUpResponseDto;
import com.cognizant.taxease.entity.User;
import com.cognizant.taxease.entity.entityEnum.StatusBasic;
import com.cognizant.taxease.entity.entityEnum.UserRole;
import com.cognizant.taxease.repository.UserRepository;
import com.cognizant.taxease.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements com.cognizant.taxease.service.AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    public SignUpResponseDto signUp(SignUpRequestDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role(UserRole.Taxpayer)
                .status(StatusBasic.Active)
                .build();

        userRepository.save(user);

        return new SignUpResponseDto(user.getId(), user.getEmail());
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = (User) auth.getPrincipal();
        String token = authUtil.generateToken(user);

        return new LoginResponseDto(token, user.getId(), user.getRole().name());
    }
}