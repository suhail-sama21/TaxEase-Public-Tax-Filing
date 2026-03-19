package com.cognizant.taxease.config;
import com.cognizant.taxease.entity.entityEnum.UserRole;
import com.cognizant.taxease.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity
                .csrf(csrfConfig->csrfConfig.disable())
                .sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                            auth
                                .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/api/taxpayer/**").hasRole("TAXPAYER")
                                    .requestMatchers("/api/compliance/**").hasRole("COMPLIANCE")
                                    .requestMatchers("/api/documents/**").hasRole("TAXPAYER")
                                    .requestMatchers("/api/filings/**").hasAnyRole("MANAGER","TAXPAYER")
                                    .requestMatchers("/api/report/**").hasAnyRole("ADMIN")
                                    .requestMatchers("/api/payments/**").hasRole("TAXPAYER")
                                    .requestMatchers("/api/notification/**").hasRole("TAXPAYER")
                                .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
