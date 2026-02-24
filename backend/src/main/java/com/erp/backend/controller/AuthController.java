package com.erp.backend.controller;

import jakarta.validation.Valid;

import com.erp.backend.config.jwt.JwtUtil;
import com.erp.backend.dto.LoginRequest;
import com.erp.backend.dto.LoginResponse;
import com.erp.backend.entity.User;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        try {
            // 1️⃣ Authenticate credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // 2️⃣ Fetch User entity
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found"));

            // 3️⃣ Generate JWT token
            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(
                    new LoginResponse(token, "Login successful")
            );

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse(null, "Invalid email or password"));
        }
    }
}